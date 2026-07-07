package au.edu.jcu.cp3406.studyquest.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertReviewState(reviewState: ReviewStateEntity)

    @Query("SELECT * FROM review_states WHERE questionId = :questionId")
    suspend fun findByQuestionId(questionId: Long): ReviewStateEntity?

    @Query("SELECT * FROM review_states WHERE mastered = 0 ORDER BY timesMissed DESC, lastReviewedAt DESC")
    fun observeOpenReviewStates(): Flow<List<ReviewStateEntity>>

    @Query("UPDATE review_states SET mastered = 1, lastReviewedAt = :reviewedAt WHERE questionId = :questionId")
    suspend fun markMastered(questionId: Long, reviewedAt: Long)

    @Query("DELETE FROM review_states")
    suspend fun clearReviewStates()
}

