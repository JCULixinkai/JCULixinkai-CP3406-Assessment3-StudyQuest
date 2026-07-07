package au.edu.jcu.cp3406.studyquest.model

data class AnswerResult(
    val question: Question,
    val selectedAnswer: String,
    val isCorrect: Boolean,
    val explanation: String,
)

data class TopicPerformance(
    val categoryId: String,
    val categoryName: String,
    val attempts: Int,
    val correct: Int,
) {
    val accuracyPercent: Int = if (attempts == 0) 0 else ((correct.toDouble() / attempts) * 100).toInt()
}

data class RecentAttempt(
    val questionText: String,
    val categoryName: String,
    val selectedAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean,
    val answeredAt: Long,
)

data class DashboardStats(
    val totalAnswered: Int = 0,
    val totalCorrect: Int = 0,
    val overallAccuracyPercent: Int = 0,
    val currentStreak: Int = 0,
    val completedToday: Int = 0,
    val dailyGoal: Int = 10,
    val weakestTopic: TopicPerformance? = null,
    val topicPerformance: List<TopicPerformance> = emptyList(),
    val recentAttempts: List<RecentAttempt> = emptyList(),
)

data class ReviewQuestion(
    val question: Question,
    val timesMissed: Int,
    val lastReviewedAt: Long,
)

