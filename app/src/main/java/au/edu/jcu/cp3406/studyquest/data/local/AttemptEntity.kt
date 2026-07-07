package au.edu.jcu.cp3406.studyquest.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import au.edu.jcu.cp3406.studyquest.model.RecentAttempt

@Entity(
    tableName = "attempts",
    indices = [Index("questionId"), Index("sessionId"), Index("categoryId")],
)
data class AttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val questionId: Long,
    val questionText: String,
    val categoryId: String,
    val categoryName: String,
    val selectedAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean,
    val answeredAt: Long,
    val sessionId: Long,
    val timeSpentSeconds: Int,
) {
    fun toRecentAttempt(): RecentAttempt = RecentAttempt(
        questionText = questionText,
        categoryName = categoryName,
        selectedAnswer = selectedAnswer,
        correctAnswer = correctAnswer,
        isCorrect = isCorrect,
        answeredAt = answeredAt,
    )
}

