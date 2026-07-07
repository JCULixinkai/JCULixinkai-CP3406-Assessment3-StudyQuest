package au.edu.jcu.cp3406.studyquest.data.repository

import au.edu.jcu.cp3406.studyquest.model.AnswerResult
import au.edu.jcu.cp3406.studyquest.model.DashboardStats
import au.edu.jcu.cp3406.studyquest.model.Difficulty
import au.edu.jcu.cp3406.studyquest.model.ReviewQuestion
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {
    fun dashboardStats(): Flow<DashboardStats>
    fun reviewQuestions(): Flow<List<ReviewQuestion>>
    suspend fun startSession(categoryId: String, difficulty: Difficulty, totalQuestions: Int): Long
    suspend fun recordAnswer(result: AnswerResult, sessionId: Long, timeSpentSeconds: Int)
    suspend fun completeSession(sessionId: Long, correctCount: Int)
    suspend fun markMastered(questionId: Long)
    suspend fun clearProgress()
}

