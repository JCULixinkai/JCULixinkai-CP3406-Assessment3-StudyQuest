package au.edu.jcu.cp3406.studyquest.data.repository

import au.edu.jcu.cp3406.studyquest.data.local.AttemptDao
import au.edu.jcu.cp3406.studyquest.data.local.AttemptEntity
import au.edu.jcu.cp3406.studyquest.data.local.QuestionDao
import au.edu.jcu.cp3406.studyquest.data.local.QuizSessionDao
import au.edu.jcu.cp3406.studyquest.data.local.QuizSessionEntity
import au.edu.jcu.cp3406.studyquest.data.local.ReviewStateDao
import au.edu.jcu.cp3406.studyquest.data.local.ReviewStateEntity
import au.edu.jcu.cp3406.studyquest.domain.ReviewSelector
import au.edu.jcu.cp3406.studyquest.domain.StatsCalculator
import au.edu.jcu.cp3406.studyquest.model.AnswerResult
import au.edu.jcu.cp3406.studyquest.model.DashboardStats
import au.edu.jcu.cp3406.studyquest.model.Difficulty
import au.edu.jcu.cp3406.studyquest.model.ReviewQuestion
import au.edu.jcu.cp3406.studyquest.model.UserSettings
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class DefaultProgressRepository @Inject constructor(
    private val attemptDao: AttemptDao,
    private val questionDao: QuestionDao,
    private val sessionDao: QuizSessionDao,
    private val reviewStateDao: ReviewStateDao,
    private val settingsRepository: SettingsRepository,
    private val statsCalculator: StatsCalculator,
    private val reviewSelector: ReviewSelector,
) : ProgressRepository {
    override fun dashboardStats(): Flow<DashboardStats> = combine(
        attemptDao.observeAttempts(),
        settingsRepository.settings,
    ) { attempts, settings ->
        statsCalculator.calculate(attempts, settings)
    }

    override fun reviewQuestions(): Flow<List<ReviewQuestion>> = combine(
        reviewStateDao.observeOpenReviewStates(),
        questionDao.observeAllQuestions(),
    ) { reviewStates, questions ->
        val questionById = questions.associateBy { it.id }
        reviewSelector.prioritize(
            reviewStates.mapNotNull { state ->
                questionById[state.questionId]?.let { question ->
                    ReviewQuestion(
                        question = question.toModel(),
                        timesMissed = state.timesMissed,
                        lastReviewedAt = state.lastReviewedAt,
                    )
                }
            },
        )
    }

    override suspend fun startSession(
        categoryId: String,
        difficulty: Difficulty,
        totalQuestions: Int,
    ): Long {
        val now = System.currentTimeMillis()
        sessionDao.upsertSession(
            QuizSessionEntity(
                id = now,
                startedAt = now,
                completedAt = null,
                categoryId = categoryId,
                difficulty = difficulty.name,
                totalQuestions = totalQuestions,
                correctCount = 0,
            ),
        )
        return now
    }

    override suspend fun recordAnswer(
        result: AnswerResult,
        sessionId: Long,
        timeSpentSeconds: Int,
    ) {
        val now = System.currentTimeMillis()
        attemptDao.insertAttempt(
            AttemptEntity(
                questionId = result.question.id,
                questionText = result.question.questionText,
                categoryId = result.question.categoryId,
                categoryName = result.question.categoryName,
                selectedAnswer = result.selectedAnswer,
                correctAnswer = result.question.correctAnswer,
                isCorrect = result.isCorrect,
                answeredAt = now,
                sessionId = sessionId,
                timeSpentSeconds = timeSpentSeconds.coerceAtLeast(1),
            ),
        )

        if (!result.isCorrect) {
            val existing = reviewStateDao.findByQuestionId(result.question.id)
            reviewStateDao.upsertReviewState(
                ReviewStateEntity(
                    questionId = result.question.id,
                    timesMissed = (existing?.timesMissed ?: 0) + 1,
                    mastered = false,
                    lastReviewedAt = now,
                ),
            )
        }
    }

    override suspend fun completeSession(sessionId: Long, correctCount: Int) {
        sessionDao.completeSession(
            sessionId = sessionId,
            completedAt = System.currentTimeMillis(),
            correctCount = correctCount,
        )
    }

    override suspend fun markMastered(questionId: Long) {
        reviewStateDao.markMastered(questionId, System.currentTimeMillis())
    }

    override suspend fun clearProgress() {
        attemptDao.clearAttempts()
        sessionDao.clearSessions()
        reviewStateDao.clearReviewStates()
    }
}

