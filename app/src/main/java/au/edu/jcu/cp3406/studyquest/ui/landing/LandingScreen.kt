package au.edu.jcu.cp3406.studyquest.ui.landing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import au.edu.jcu.cp3406.studyquest.model.DashboardStats
import au.edu.jcu.cp3406.studyquest.ui.components.ActionRow
import au.edu.jcu.cp3406.studyquest.ui.components.EmptyState
import au.edu.jcu.cp3406.studyquest.ui.components.MetricCard
import au.edu.jcu.cp3406.studyquest.ui.components.PageTitle
import au.edu.jcu.cp3406.studyquest.ui.components.ProgressCard
import au.edu.jcu.cp3406.studyquest.ui.components.ScreenContainer
import au.edu.jcu.cp3406.studyquest.ui.components.SectionHeading

@Composable
fun LandingScreen(
    stats: DashboardStats,
    onStartQuiz: () -> Unit,
    onReview: () -> Unit,
    onStats: () -> Unit,
) {
    LazyColumn {
        item {
            ScreenContainer {
                PageTitle(
                    title = "CP3406-Assessment3-StudyQuest",
                    subtitle = "Practice, review mistakes, and track learning progress in one local-first app.",
                )
                ActionRow(
                    primaryLabel = "Start Quiz",
                    onPrimary = onStartQuiz,
                    secondaryLabel = "Review Mistakes",
                    onSecondary = onReview,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    MetricCard(
                        label = "Answered",
                        value = stats.totalAnswered.toString(),
                        supportingText = "Questions completed",
                        modifier = Modifier.weight(1f),
                    )
                    MetricCard(
                        label = "Accuracy",
                        value = "${stats.overallAccuracyPercent}%",
                        supportingText = "${stats.totalCorrect} correct",
                        modifier = Modifier.weight(1f),
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    MetricCard(
                        label = "Streak",
                        value = stats.currentStreak.toString(),
                        supportingText = "Active study days",
                        modifier = Modifier.weight(1f),
                    )
                    MetricCard(
                        label = "Today",
                        value = "${stats.completedToday}/${stats.dailyGoal}",
                        supportingText = "Daily goal progress",
                        modifier = Modifier.weight(1f),
                    )
                }
                ProgressCard(
                    label = "Daily goal",
                    valueLabel = "${stats.completedToday}/${stats.dailyGoal}",
                    percent = if (stats.dailyGoal == 0) 0 else (stats.completedToday * 100 / stats.dailyGoal),
                )
                if (stats.weakestTopic == null) {
                    EmptyState(
                        title = "No learning data yet",
                        message = "Complete a short quiz to unlock topic insights and mistake review.",
                        actionLabel = "Start Quiz",
                        onAction = onStartQuiz,
                    )
                } else {
                    EmptyState(
                        title = "Recommended review",
                        message = "${stats.weakestTopic.categoryName} is your weakest current topic at ${stats.weakestTopic.accuracyPercent}% accuracy.",
                        actionLabel = "View Stats",
                        onAction = onStats,
                    )
                }
                SectionHeading("Recent learning")
            }
        }
        if (stats.recentAttempts.isEmpty()) {
            item {
                ScreenContainer {
                    Text("Recent answers will appear here after your first quiz.")
                }
            }
        } else {
            items(stats.recentAttempts.take(4)) { attempt ->
                ScreenContainer {
                    ProgressCard(
                        label = attempt.categoryName,
                        valueLabel = if (attempt.isCorrect) "Correct" else "Review",
                        percent = if (attempt.isCorrect) 100 else 35,
                    )
                }
            }
        }
    }
}
