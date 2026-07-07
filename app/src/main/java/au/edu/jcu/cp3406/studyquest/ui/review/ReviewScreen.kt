package au.edu.jcu.cp3406.studyquest.ui.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import au.edu.jcu.cp3406.studyquest.model.ReviewQuestion
import au.edu.jcu.cp3406.studyquest.ui.components.EmptyState
import au.edu.jcu.cp3406.studyquest.ui.components.PageTitle
import au.edu.jcu.cp3406.studyquest.ui.components.ScreenContainer
import au.edu.jcu.cp3406.studyquest.ui.components.SectionHeading

@Composable
fun ReviewScreen(
    reviewQuestions: List<ReviewQuestion>,
    onMarkMastered: (Long) -> Unit,
    onStartQuiz: () -> Unit,
) {
    LazyColumn {
        item {
            ScreenContainer {
                PageTitle(
                    title = "Review Mistakes",
                    subtitle = "Focus on questions that were missed before and decide when they are mastered.",
                )
                if (reviewQuestions.isEmpty()) {
                    EmptyState(
                        title = "No mistakes to review",
                        message = "Wrong answers will appear here. Complete a quiz to build a review queue.",
                        actionLabel = "Start Quiz",
                        onAction = onStartQuiz,
                    )
                } else {
                    SectionHeading("${reviewQuestions.size} active review items")
                }
            }
        }
        items(reviewQuestions) { item ->
            ScreenContainer {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(
                            text = item.question.categoryName,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = item.question.questionText,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Text("Correct answer: ${item.question.correctAnswer}")
                        Text(item.question.explanation, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Missed ${item.timesMissed} time(s)", style = MaterialTheme.typography.labelMedium)
                        Button(onClick = { onMarkMastered(item.question.id) }) {
                            Text("Mark as Mastered")
                        }
                    }
                }
            }
        }
    }
}

