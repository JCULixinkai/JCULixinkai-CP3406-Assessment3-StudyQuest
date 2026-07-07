package au.edu.jcu.cp3406.studyquest.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.jcu.cp3406.studyquest.data.repository.ProgressRepository
import au.edu.jcu.cp3406.studyquest.data.repository.QuestionLoadResult
import au.edu.jcu.cp3406.studyquest.data.repository.QuestionRepository
import au.edu.jcu.cp3406.studyquest.data.repository.SettingsRepository
import au.edu.jcu.cp3406.studyquest.domain.QuizScorer
import au.edu.jcu.cp3406.studyquest.model.AnswerResult
import au.edu.jcu.cp3406.studyquest.model.Question
import au.edu.jcu.cp3406.studyquest.model.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class QuizUiState(
    val loading: Boolean = false,
    val questions: List<Question> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswer: String? = null,
    val answerResult: AnswerResult? = null,
    val message: String? = null,
    val finished: Boolean = false,
    val correctCount: Int = 0,
    val settings: UserSettings = UserSettings(),
) {
    val currentQuestion: Question? = questions.getOrNull(currentIndex)
    val progressLabel: String = if (questions.isEmpty()) "0 / 0" else "${currentIndex + 1} / ${questions.size}"
}

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val quizScorer: QuizScorer,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = mutableUiState.asStateFlow()

    private var sessionId: Long? = null
    private var questionStartedAt: Long = System.currentTimeMillis()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                mutableUiState.value = mutableUiState.value.copy(settings = settings)
            }
        }
        loadQuiz()
    }

    fun loadQuiz() {
        viewModelScope.launch {
            val settings = settingsRepository.currentSettings()
            mutableUiState.value = QuizUiState(loading = true, settings = settings)
            when (val result = questionRepository.loadQuestions(
                categoryIds = settings.enabledCategoryIds,
                difficulty = settings.preferredDifficulty,
                amount = settings.dailyGoal.coerceIn(5, 10),
            )) {
                is QuestionLoadResult.Success -> {
                    sessionId = progressRepository.startSession(
                        categoryId = result.questions.firstOrNull()?.categoryId ?: "mixed",
                        difficulty = settings.preferredDifficulty,
                        totalQuestions = result.questions.size,
                    )
                    questionStartedAt = System.currentTimeMillis()
                    mutableUiState.value = QuizUiState(
                        questions = result.questions,
                        message = result.message,
                        settings = settings,
                    )
                }

                is QuestionLoadResult.Failure -> {
                    mutableUiState.value = QuizUiState(
                        message = result.message,
                        settings = settings,
                    )
                }
            }
        }
    }

    fun selectAnswer(answer: String) {
        val state = mutableUiState.value
        if (state.answerResult != null || state.finished) return
        mutableUiState.value = state.copy(selectedAnswer = answer)
    }

    fun submitAnswer() {
        val state = mutableUiState.value
        val question = state.currentQuestion ?: return
        val selected = state.selectedAnswer ?: return
        val session = sessionId ?: return
        val result = quizScorer.score(question, selected)
        val correctCount = state.correctCount + if (result.isCorrect) 1 else 0

        viewModelScope.launch {
            progressRepository.recordAnswer(
                result = result,
                sessionId = session,
                timeSpentSeconds = ((System.currentTimeMillis() - questionStartedAt) / 1000).toInt(),
            )
            mutableUiState.value = state.copy(
                answerResult = result,
                correctCount = correctCount,
            )
        }
    }

    fun nextQuestion() {
        val state = mutableUiState.value
        if (state.currentIndex >= state.questions.lastIndex) {
            finishQuiz()
        } else {
            questionStartedAt = System.currentTimeMillis()
            mutableUiState.value = state.copy(
                currentIndex = state.currentIndex + 1,
                selectedAnswer = null,
                answerResult = null,
            )
        }
    }

    private fun finishQuiz() {
        val state = mutableUiState.value
        val session = sessionId
        mutableUiState.value = state.copy(finished = true)
        if (session != null) {
            viewModelScope.launch {
                progressRepository.completeSession(session, state.correctCount)
            }
        }
    }
}

