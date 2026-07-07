package au.edu.jcu.cp3406.studyquest.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AttemptDao {
    @Insert
    suspend fun insertAttempt(attempt: AttemptEntity)

    @Query("SELECT * FROM attempts ORDER BY answeredAt DESC")
    fun observeAttempts(): Flow<List<AttemptEntity>>

    @Query("SELECT * FROM attempts ORDER BY answeredAt DESC LIMIT :limit")
    fun observeRecentAttempts(limit: Int): Flow<List<AttemptEntity>>

    @Query("DELETE FROM attempts")
    suspend fun clearAttempts()
}

