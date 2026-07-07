package au.edu.jcu.cp3406.studyquest.domain

import au.edu.jcu.cp3406.studyquest.data.local.AttemptEntity
import au.edu.jcu.cp3406.studyquest.model.DashboardStats
import au.edu.jcu.cp3406.studyquest.model.TopicPerformance
import au.edu.jcu.cp3406.studyquest.model.UserSettings
import javax.inject.Inject

class StatsCalculator @Inject constructor(
    private val dailyGoalTracker: DailyGoalTracker,
) {
    fun calculate(
        attempts: List<AttemptEntity>,
        settings: UserSettings,
    ): DashboardStats {
        val total = attempts.size
        val correct = attempts.count { it.isCorrect }
        val topicPerformance = attempts
            .groupBy { it.categoryId }
            .map { (_, categoryAttempts) ->
                TopicPerformance(
                    categoryId = categoryAttempts.first().categoryId,
                    categoryName = categoryAttempts.first().categoryName,
                    attempts = categoryAttempts.size,
                    correct = categoryAttempts.count { it.isCorrect },
                )
            }
            .sortedBy { it.accuracyPercent }

        return DashboardStats(
            totalAnswered = total,
            totalCorrect = correct,
            overallAccuracyPercent = if (total == 0) 0 else ((correct.toDouble() / total) * 100).toInt(),
            currentStreak = dailyGoalTracker.currentStreak(attempts),
            completedToday = dailyGoalTracker.completedToday(attempts),
            dailyGoal = settings.dailyGoal,
            weakestTopic = topicPerformance.firstOrNull { it.attempts > 0 },
            topicPerformance = topicPerformance,
            recentAttempts = attempts.sortedByDescending { it.answeredAt }
                .take(8)
                .map { it.toRecentAttempt() },
        )
    }
}

