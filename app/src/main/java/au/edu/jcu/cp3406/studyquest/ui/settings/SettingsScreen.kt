package au.edu.jcu.cp3406.studyquest.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import au.edu.jcu.cp3406.studyquest.model.Difficulty
import au.edu.jcu.cp3406.studyquest.model.StudyCategories
import au.edu.jcu.cp3406.studyquest.model.UserSettings
import au.edu.jcu.cp3406.studyquest.ui.components.EmptyState
import au.edu.jcu.cp3406.studyquest.ui.components.PageTitle
import au.edu.jcu.cp3406.studyquest.ui.components.ScreenContainer
import au.edu.jcu.cp3406.studyquest.ui.components.SectionHeading

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    settings: UserSettings,
    onDailyGoalChange: (Int) -> Unit,
    onDifficultyChange: (Difficulty) -> Unit,
    onCategoryToggle: (String, Boolean) -> Unit,
    onLargeTextChange: (Boolean) -> Unit,
    onHighContrastChange: (Boolean) -> Unit,
    onReduceMotionChange: (Boolean) -> Unit,
    onInstantFeedbackChange: (Boolean) -> Unit,
    onResetProgress: () -> Unit,
) {
    LazyColumn {
        item {
            ScreenContainer {
                PageTitle(
                    title = "Settings",
                    subtitle = "Control question scope, accessibility, and local learning data.",
                )
                SectionHeading("Quiz setup")
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        Text("Daily goal", fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(5, 10, 15, 20).forEach { goal ->
                                if (settings.dailyGoal == goal) {
                                    Button(onClick = { onDailyGoalChange(goal) }) {
                                        Text(goal.toString())
                                    }
                                } else {
                                    OutlinedButton(onClick = { onDailyGoalChange(goal) }) {
                                        Text(goal.toString())
                                    }
                                }
                            }
                        }
                        Text("Difficulty", fontWeight = FontWeight.Bold)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Difficulty.entries.forEach { difficulty ->
                                FilterChip(
                                    selected = settings.preferredDifficulty == difficulty,
                                    onClick = { onDifficultyChange(difficulty) },
                                    label = { Text(difficulty.label) },
                                )
                            }
                        }
                        Text("Topics", fontWeight = FontWeight.Bold)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            StudyCategories.all.forEach { category ->
                                val selected = category.id in settings.enabledCategoryIds
                                FilterChip(
                                    selected = selected,
                                    onClick = { onCategoryToggle(category.id, !selected) },
                                    label = { Text(category.label) },
                                )
                            }
                        }
                    }
                }

                SectionHeading("Accessibility")
                SettingsSwitch(
                    title = "Large text mode",
                    subtitle = "Increase text size across the app.",
                    checked = settings.largeText,
                    onCheckedChange = onLargeTextChange,
                )
                SettingsSwitch(
                    title = "High contrast mode",
                    subtitle = "Use stronger colour contrast for readability.",
                    checked = settings.highContrast,
                    onCheckedChange = onHighContrastChange,
                )
                SettingsSwitch(
                    title = "Reduce motion",
                    subtitle = "Keep interactions calm and avoid unnecessary animation.",
                    checked = settings.reduceMotion,
                    onCheckedChange = onReduceMotionChange,
                )
                SettingsSwitch(
                    title = "Instant feedback",
                    subtitle = "Show explanations after each submitted answer.",
                    checked = settings.instantFeedback,
                    onCheckedChange = onInstantFeedbackChange,
                )

                SectionHeading("Data control")
                EmptyState(
                    title = "Local progress only",
                    message = "StudyQuest does not use accounts, names, location, or cloud sync. Reset clears attempts, sessions, and review items stored in Room.",
                    actionLabel = "Reset Progress",
                    onAction = onResetProgress,
                )
            }
        }
    }
}

@Composable
private fun SettingsSwitch(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}

