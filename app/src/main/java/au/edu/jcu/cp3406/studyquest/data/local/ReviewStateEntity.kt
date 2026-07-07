package au.edu.jcu.cp3406.studyquest.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "review_states")
data class ReviewStateEntity(
    @PrimaryKey val questionId: Long,
    val timesMissed: Int,
    val mastered: Boolean,
    val lastReviewedAt: Long,
)

