package au.edu.jcu.cp3406.studyquest.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSession(session: QuizSessionEntity)

    @Query("SELECT * FROM quiz_sessions ORDER BY startedAt DESC")
    fun observeSessions(): Flow<List<QuizSessionEntity>>

    @Query(
        """
        UPDATE quiz_sessions
        SET completedAt = :completedAt, correctCount = :correctCount
        WHERE id = :sessionId
        """,
    )
    suspend fun completeSession(sessionId: Long, completedAt: Long, correctCount: Int)

    @Query("DELETE FROM quiz_sessions")
    suspend fun clearSessions()
}

