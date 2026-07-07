package au.edu.jcu.cp3406.studyquest.ui.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import au.edu.jcu.cp3406.studyquest.model.DashboardStats
import au.edu.jcu.cp3406.studyquest.ui.components.EmptyState
import au.edu.jcu.cp3406.studyquest.ui.components.MetricCard
import au.edu.jcu.cp3406.studyquest.ui.components.PageTitle
import au.edu.jcu.cp3406.studyquest.ui.components.ProgressCard
import au.edu.jcu.cp3406.studyquest.ui.components.ScreenContainer
import au.edu.jcu.cp3406.studyquest.ui.components.SectionHeading

@Composable
fun StatsScreen(stats: DashboardStats) {
    LazyColumn {
        item {
            ScreenContainer {
                PageTitle(
                    title = "User Statistics",
                    subtitle = "Progress is calculated from local Room data and stays on this device.",
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    MetricCard("Total", stats.totalAnswered.toString(), "Questions answered", Modifier.weight(1f))
                    MetricCard("Accuracy", "${stats.overallAccuracyPercent}%", "Overall score", Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    MetricCard("Streak", stats.currentStreak.toString(), "Learning days", Modifier.weight(1f))
                    MetricCard("Today", "${stats.completedToday}/${stats.dailyGoal}", "Daily target", Modifier.weight(1f))
                }
                SectionHeading("Topic performance")
            }
        }
        if (stats.topicPerformance.isEmpty()) {
            item {
                ScreenContainer {
                    EmptyState(
                        title = "Statistics need answers",
                        message = "StudyQuest will show topic accuracy after your first quiz.",
                    )
                }
            }
        } else {
            items(stats.topicPerformance) { topic ->
                ScreenContainer {
                    ProgressCard(
                        label = topic.categoryName,
                        valueLabel = "${topic.accuracyPercent}% (${topic.correct}/${topic.attempts})",
                        percent = topic.accuracyPercent,
                    )
                }
            }
        }
        if (stats.recentAttempts.isNotEmpty()) {
            item {
                ScreenContainer {
                    SectionHeading("Recent answers")
                }
            }
            items(stats.recentAttempts) { attempt ->
                ScreenContainer {
                    ProgressCard(
                        label = attempt.questionText,
                        valueLabel = if (attempt.isCorrect) "Correct" else "Incorrect",
                        percent = if (attempt.isCorrect) 100 else 20,
                    )
                }
            }
        }
    }
}

