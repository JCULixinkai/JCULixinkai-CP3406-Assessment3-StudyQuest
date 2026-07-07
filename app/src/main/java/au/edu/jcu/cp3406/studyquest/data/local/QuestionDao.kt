package au.edu.jcu.cp3406.studyquest.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuestions(questions: List<QuestionEntity>): List<Long>

    @Query("SELECT * FROM questions ORDER BY fetchedAt DESC")
    fun observeAllQuestions(): Flow<List<QuestionEntity>>

    @Query(
        """
        SELECT * FROM questions
        WHERE categoryId IN (:categoryIds)
        ORDER BY fetchedAt DESC
        LIMIT :limit
        """,
    )
    suspend fun cachedByCategories(categoryIds: List<String>, limit: Int): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE id = :id")
    suspend fun findById(id: Long): QuestionEntity?

    @Query("DELETE FROM questions WHERE source = 'seed'")
    suspend fun clearSeedQuestions()
}

