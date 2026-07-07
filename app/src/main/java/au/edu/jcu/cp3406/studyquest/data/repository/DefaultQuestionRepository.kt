package au.edu.jcu.cp3406.studyquest.data.repository

import au.edu.jcu.cp3406.studyquest.data.local.QuestionDao
import au.edu.jcu.cp3406.studyquest.data.local.normalizedQuestionKey
import au.edu.jcu.cp3406.studyquest.data.local.toEntity
import au.edu.jcu.cp3406.studyquest.data.remote.TriviaApiService
import au.edu.jcu.cp3406.studyquest.data.remote.dto.TriviaQuestionDto
import au.edu.jcu.cp3406.studyquest.domain.SeedQuestions
import au.edu.jcu.cp3406.studyquest.model.Difficulty
import au.edu.jcu.cp3406.studyquest.model.Question
import au.edu.jcu.cp3406.studyquest.model.StudyCategories
import com.squareup.moshi.JsonDataException
import java.io.IOException
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

class DefaultQuestionRepository @Inject constructor(
    private val apiService: TriviaApiService,
    private val questionDao: QuestionDao,
) : QuestionRepository {
    override suspend fun loadQuestions(
        categoryIds: List<String>,
        difficulty: Difficulty,
        amount: Int,
    ): QuestionLoadResult {
        val enabledCategories = categoryIds.ifEmpty { StudyCategories.all.map { it.id } }
        val category = StudyCategories.byId(enabledCategories.random())

        return try {
            val response = apiService.fetchQuestions(
                amount = amount.coerceIn(1, 10),
                category = category.apiCategory,
                difficulty = difficulty.apiValue,
            )
            if (response.responseCode != 0 || response.results.isEmpty()) {
                cachedOrSeeded(
                    enabledCategories,
                    amount,
                    "Open Trivia DB had no matching live questions, so StudyQuest loaded saved practice questions.",
                )
            } else {
                val questions = response.results
                    .map { it.toQuestion(categoryId = category.id, categoryName = category.label) }
                    .distinctBy { normalizedQuestionKey(it.questionText) }
                questionDao.insertQuestions(questions.map { it.toEntity() })
                val savedQuestions = questionDao.cachedByCategories(listOf(category.id), amount)
                    .map { it.toModel() }
                QuestionLoadResult.Success(savedQuestions.ifEmpty { questions }.take(amount))
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: IOException) {
            cachedOrSeeded(
                enabledCategories,
                amount,
                "No network connection. StudyQuest loaded cached or offline practice questions.",
            )
        } catch (_: HttpException) {
            cachedOrSeeded(
                enabledCategories,
                amount,
                "The quiz service did not respond. StudyQuest loaded cached or offline questions.",
            )
        } catch (_: JsonDataException) {
            cachedOrSeeded(
                enabledCategories,
                amount,
                "The quiz service returned unexpected data. StudyQuest loaded offline questions.",
            )
        }
    }

    override suspend fun findQuestion(id: Long): Question? = questionDao.findById(id)?.toModel()

    private suspend fun cachedOrSeeded(
        categoryIds: List<String>,
        amount: Int,
        message: String,
    ): QuestionLoadResult.Success {
        val cached = questionDao.cachedByCategories(categoryIds, amount).map { it.toModel() }
        if (cached.isNotEmpty()) {
            return QuestionLoadResult.Success(cached, message)
        }

        val seeds = SeedQuestions.all()
            .filter { it.categoryId in categoryIds }
            .ifEmpty { SeedQuestions.all() }
            .take(amount)

        questionDao.insertQuestions(seeds.map { it.toEntity(fetchedAt = 0L) })
        val savedSeeds = questionDao.cachedByCategories(categoryIds, amount).map { it.toModel() }
        return QuestionLoadResult.Success(savedSeeds.ifEmpty { seeds }, message)
    }

    private fun TriviaQuestionDto.toQuestion(
        categoryId: String,
        categoryName: String,
    ): Question {
        val decodedCorrect = correctAnswer.decode()
        val allAnswers = (incorrectAnswers.map { it.decode() } + decodedCorrect).shuffled()
        return Question(
            source = "Open Trivia DB",
            categoryId = categoryId,
            categoryName = categoryName,
            difficulty = Difficulty.entries.firstOrNull { it.apiValue == difficulty.decode().lowercase() } ?: Difficulty.Mixed,
            questionText = question.decode(),
            answers = allAnswers,
            correctAnswer = decodedCorrect,
            explanation = "This question came from Open Trivia DB. Review the correct answer and compare it with your choice.",
        )
    }

    private fun String.decode(): String = URLDecoder.decode(this, StandardCharsets.UTF_8.name())
}
