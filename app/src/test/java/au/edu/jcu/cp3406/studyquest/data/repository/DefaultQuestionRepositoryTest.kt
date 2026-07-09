package au.edu.jcu.cp3406.studyquest.data.repository

import au.edu.jcu.cp3406.studyquest.data.local.QuestionDao
import au.edu.jcu.cp3406.studyquest.data.local.QuestionEntity
import au.edu.jcu.cp3406.studyquest.data.remote.TriviaApiService
import au.edu.jcu.cp3406.studyquest.data.remote.dto.TriviaQuestionDto
import au.edu.jcu.cp3406.studyquest.data.remote.dto.TriviaResponseDto
import au.edu.jcu.cp3406.studyquest.model.Difficulty
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DefaultQuestionRepositoryTest {
    @Test
    fun `live api success maps and caches questions`() = runTest {
        val dao = FakeQuestionDao()
        val api = FakeTriviaApiService(
            response = TriviaResponseDto(
                responseCode = 0,
                results = listOf(
                    TriviaQuestionDto(
                        category = "Science: Computers",
                        type = "multiple",
                        difficulty = "easy",
                        question = "What%20does%20Room%20store%3F",
                        correctAnswer = "Local%20data",
                        incorrectAnswers = listOf("HTTP%20requests", "Vector%20icons", "Screen%20brightness"),
                    ),
                ),
            ),
        )
        val repository = DefaultQuestionRepository(api, dao)

        val result = repository.loadQuestions(
            categoryIds = listOf("computers"),
            difficulty = Difficulty.Easy,
            amount = 20,
        )

        assertThat(api.requestedAmount).isEqualTo(20)
        assertThat(result).isInstanceOf(QuestionLoadResult.Success::class.java)
        val questions = (result as QuestionLoadResult.Success).questions
        assertThat(questions).hasSize(1)
        assertThat(questions.first().questionText).isEqualTo("What does Room store?")
        assertThat(questions.first().correctAnswer).isEqualTo("Local data")
        assertThat(dao.insertedQuestions).hasSize(1)
    }

    @Test
    fun `api no results falls back to cached questions first`() = runTest {
        val dao = FakeQuestionDao()
        dao.insertQuestions(
            listOf(
                QuestionEntity(
                    source = "Open Trivia DB",
                    sourceId = "cached-room-question",
                    categoryId = "computers",
                    categoryName = "Computer Basics",
                    difficulty = Difficulty.Easy.name,
                    questionText = "Which Android library keeps structured local data?",
                    normalizedQuestion = "which android library keeps structured local data",
                    answers = listOf("Room", "Retrofit", "Moshi", "OkHttp"),
                    correctAnswer = "Room",
                    explanation = "Room stores structured local data on the device.",
                    fetchedAt = 100,
                ),
            ),
        )
        val repository = DefaultQuestionRepository(
            apiService = FakeTriviaApiService(response = TriviaResponseDto(responseCode = 1)),
            questionDao = dao,
        )

        val result = repository.loadQuestions(
            categoryIds = listOf("computers"),
            difficulty = Difficulty.Easy,
            amount = 5,
        )

        val success = result as QuestionLoadResult.Success
        assertThat(success.questions).hasSize(1)
        assertThat(success.questions.first().questionText)
            .isEqualTo("Which Android library keeps structured local data?")
        assertThat(success.questions.first().source).isEqualTo("Open Trivia DB")
    }

    @Test
    fun `api no results falls back to seeded questions`() = runTest {
        val repository = DefaultQuestionRepository(
            apiService = FakeTriviaApiService(response = TriviaResponseDto(responseCode = 1)),
            questionDao = FakeQuestionDao(),
        )

        val result = repository.loadQuestions(
            categoryIds = listOf("computers"),
            difficulty = Difficulty.Easy,
            amount = 5,
        )

        assertThat(result).isInstanceOf(QuestionLoadResult.Success::class.java)
        val success = result as QuestionLoadResult.Success
        assertThat(success.message).contains("no matching live questions")
        assertThat(success.questions).isNotEmpty()
        assertThat(success.questions.all { it.source == "seed" }).isTrue()
    }
}

private class FakeTriviaApiService(
    private val response: TriviaResponseDto,
) : TriviaApiService {
    var requestedAmount: Int = 0

    override suspend fun fetchQuestions(
        amount: Int,
        category: Int,
        difficulty: String?,
        type: String,
        encode: String,
    ): TriviaResponseDto {
        requestedAmount = amount
        return response
    }
}

private class FakeQuestionDao : QuestionDao {
    private val questions = mutableListOf<QuestionEntity>()
    private val questionFlow = MutableStateFlow<List<QuestionEntity>>(emptyList())
    private var nextId = 1L

    val insertedQuestions: List<QuestionEntity>
        get() = questions

    override suspend fun insertQuestions(questions: List<QuestionEntity>): List<Long> {
        val insertedIds = mutableListOf<Long>()
        questions.forEach { question ->
            if (this.questions.none { it.normalizedQuestion == question.normalizedQuestion }) {
                val inserted = question.copy(id = nextId++)
                this.questions += inserted
                insertedIds += inserted.id
            } else {
                insertedIds += -1
            }
        }
        questionFlow.value = this.questions.toList()
        return insertedIds
    }

    override fun observeAllQuestions(): Flow<List<QuestionEntity>> = questionFlow.asStateFlow()

    override suspend fun cachedByCategories(
        categoryIds: List<String>,
        limit: Int,
    ): List<QuestionEntity> = questions
        .filter { it.categoryId in categoryIds }
        .sortedByDescending { it.fetchedAt }
        .take(limit)

    override suspend fun findById(id: Long): QuestionEntity? =
        questions.firstOrNull { it.id == id }

    override suspend fun clearSeedQuestions() {
        questions.removeAll { it.source == "seed" }
        questionFlow.value = questions.toList()
    }
}
