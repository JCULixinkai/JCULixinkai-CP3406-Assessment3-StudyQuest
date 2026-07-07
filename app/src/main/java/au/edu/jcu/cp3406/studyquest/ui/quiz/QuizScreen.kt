package au.edu.jcu.cp3406.studyquest.ui.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import au.edu.jcu.cp3406.studyquest.ui.components.ActionRow
import au.edu.jcu.cp3406.studyquest.ui.components.EmptyState
import au.edu.jcu.cp3406.studyquest.ui.components.PageTitle
import au.edu.jcu.cp3406.studyquest.ui.components.ProgressCard
import au.edu.jcu.cp3406.studyquest.ui.components.ScreenContainer

@Composable
fun QuizScreen(
    uiState: QuizUiState,
    onReload: () -> Unit,
    onSelectAnswer: (String) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextQuestion: () -> Unit,
    onReview: () -> Unit,
    onStats: () -> Unit,
) {
    LazyColumn {
        item {
            ScreenContainer {
                PageTitle(
                    title = "Quiz Activity",
                    subtitle = "Answer multiple-choice questions and get clear feedback for study decisions.",
                )
                uiState.message?.let { message ->
                    EmptyState(title = "Learning source note", message = message)
                }
                when {
                    uiState.loading -> {
                        CircularProgressIndicator()
                        Text("Loading practice questions...")
                    }

                    uiState.finished -> {
                        EmptyState(
                            title = "Quiz complete",
                            message = "You scored ${uiState.correctCount}/${uiState.questions.size}. Review mistakes or inspect your statistics.",
                        )
                        ActionRow(
                            primaryLabel = "New Quiz",
                            onPrimary = onReload,
                            secondaryLabel = "View Stats",
                            onSecondary = onStats,
                        )
                        OutlinedButton(onClick = onReview) {
                            Text("Review Mistakes")
                        }
                    }

                    uiState.currentQuestion == null -> {
                        EmptyState(
                            title = "No questions available",
                            message = "StudyQuest could not load live, cached, or offline questions.",
                            actionLabel = "Try Again",
                            onAction = onReload,
                        )
                    }

                    else -> {
                        val question = uiState.currentQuestion
                        ProgressCard(
                            label = question.categoryName,
                            valueLabel = uiState.progressLabel,
                            percent = ((uiState.currentIndex + 1) * 100 / uiState.questions.size),
                        )
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                Text(
                                    text = question.difficulty.label,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                                Text(
                                    text = question.questionText,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                )
                                question.answers.forEach { answer ->
                                    val isSelected = uiState.selectedAnswer == answer
                                    val result = uiState.answerResult
                                    val isCorrect = result?.question?.correctAnswer == answer
                                    val label = when {
                                        result != null && isCorrect -> "$answer - correct"
                                        result != null && isSelected && !isCorrect -> "$answer - your choice"
                                        else -> answer
                                    }
                                    OutlinedButton(
                                        onClick = { onSelectAnswer(answer) },
                                        enabled = result == null,
                                        modifier = Modifier.fillMaxWidth(),
                                    ) {
                                        Text(label)
                                    }
                                }
                            }
                        }
                        uiState.answerResult?.let { result ->
                            EmptyState(
                                title = if (result.isCorrect) "Correct" else "Review this",
                                message = if (uiState.settings.instantFeedback) {
                                    result.explanation
                                } else {
                                    "Your answer has been saved. Detailed explanations can be reviewed after the quiz."
                                },
                            )
                        }
                        if (uiState.answerResult == null) {
                            Button(
                                onClick = onSubmitAnswer,
                                enabled = uiState.selectedAnswer != null,
                            ) {
                                Text("Submit Answer")
                            }
                        } else {
                            Button(onClick = onNextQuestion) {
                                Text(if (uiState.currentIndex == uiState.questions.lastIndex) "Finish Quiz" else "Next Question")
                            }
                        }
                    }
                }
            }
        }
    }
}
