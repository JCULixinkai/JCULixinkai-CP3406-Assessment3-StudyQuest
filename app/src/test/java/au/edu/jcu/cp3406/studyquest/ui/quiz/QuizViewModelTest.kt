package au.edu.jcu.cp3406.studyquest.ui.quiz

import au.edu.jcu.cp3406.studyquest.MainDispatcherRule
import au.edu.jcu.cp3406.studyquest.data.repository.ProgressRepository
import au.edu.jcu.cp3406.studyquest.data.repository.QuestionLoadResult
import au.edu.jcu.cp3406.studyquest.data.repository.QuestionRepository
import au.edu.jcu.cp3406.studyquest.data.repository.SettingsRepository
import au.edu.jcu.cp3406.studyquest.domain.QuizScorer
import au.edu.jcu.cp3406.studyquest.model.AnswerResult
import au.edu.jcu.cp3406.studyquest.model.DashboardStats
import au.edu.jcu.cp3406.studyquest.model.Difficulty
import au.edu.jcu.cp3406.studyquest.model.Question
import au.edu.jcu.cp3406.studyquest.model.ReviewQuestion
import au.edu.jcu.cp3406.studyquest.model.UserSettings
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `loads questions and records submitted answer`() = runTest {
        val progressRepository = FakeProgressRepository()
        val viewModel = QuizViewModel(
            questionRepository = FakeQuestionRepository(),
            progressRepository = progressRepository,
            settingsRepository = FakeSettingsRepository(),
            quizScorer = QuizScorer(),
        )

        advanceUntilIdle()

        assertThat(viewModel.uiState.value.questions).hasSize(1)

        viewModel.selectAnswer("Room")
        viewModel.submitAnswer()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.answerResult?.isCorrect).isTrue()
        assertThat(progressRepository.recordedAnswers).hasSize(1)
    }

    private class FakeQuestionRepository : QuestionRepository {
        override suspend fun loadQuestions(
            categoryIds: List<String>,
            difficulty: Difficulty,
            amount: Int,
        ): QuestionLoadResult = QuestionLoadResult.Success(listOf(sampleQuestion()))

        override suspend fun findQuestion(id: Long): Question? = sampleQuestion()
    }

    private class FakeSettingsRepository : SettingsRepository {
        private val mutableSettings = MutableStateFlow(UserSettings(dailyGoal = 5))
        override val settings: Flow<UserSettings> = mutableSettings
        override suspend fun currentSettings(): UserSettings = mutableSettings.value
        override suspend fun updateDailyGoal(goal: Int) {}
        override suspend fun updateDifficulty(difficulty: Difficulty) {}
        override suspend fun setCategoryEnabled(categoryId: String, enabled: Boolean) {}
        override suspend fun updateLargeText(enabled: Boolean) {}
        override suspend fun updateHighContrast(enabled: Boolean) {}
        override suspend fun updateReduceMotion(enabled: Boolean) {}
        override suspend fun updateInstantFeedback(enabled: Boolean) {}
    }

    private class FakeProgressRepository : ProgressRepository {
        val recordedAnswers = mutableListOf<AnswerResult>()
        override fun dashboardStats(): Flow<DashboardStats> = flowOf(DashboardStats())
        override fun reviewQuestions(): Flow<List<ReviewQuestion>> = flowOf(emptyList())
        override suspend fun startSession(categoryId: String, difficulty: Difficulty, totalQuestions: Int): Long = 10L
        override suspend fun recordAnswer(result: AnswerResult, sessionId: Long, timeSpentSeconds: Int) {
            recordedAnswers += result
        }
        override suspend fun completeSession(sessionId: Long, correctCount: Int) {}
        override suspend fun markMastered(questionId: Long) {}
        override suspend fun clearProgress() {}
    }
}

private fun sampleQuestion(): Question = Question(
    id = 1,
    source = "seed",
    categoryId = "computers",
    categoryName = "Computer Basics",
    difficulty = Difficulty.Easy,
    questionText = "Which library stores local data?",
    answers = listOf("Room", "Retrofit", "Moshi", "OkHttp"),
    correctAnswer = "Room",
    explanation = "Room stores structured local data.",
)

