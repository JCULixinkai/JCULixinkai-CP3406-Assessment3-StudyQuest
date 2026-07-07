package au.edu.jcu.cp3406.studyquest.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_sessions")
data class QuizSessionEntity(
    @PrimaryKey val id: Long,
    val startedAt: Long,
    val completedAt: Long?,
    val categoryId: String,
    val difficulty: String,
    val totalQuestions: Int,
    val correctCount: Int,
)

