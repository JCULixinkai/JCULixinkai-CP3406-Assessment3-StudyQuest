package au.edu.jcu.cp3406.studyquest.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import au.edu.jcu.cp3406.studyquest.domain.QuizScorer
import au.edu.jcu.cp3406.studyquest.model.Difficulty
import au.edu.jcu.cp3406.studyquest.model.Question
import au.edu.jcu.cp3406.studyquest.ui.quiz.QuizScreen
import au.edu.jcu.cp3406.studyquest.ui.quiz.QuizUiState
import au.edu.jcu.cp3406.studyquest.ui.theme.StudyQuestTheme
import org.junit.Rule
import org.junit.Test

class QuizScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun quizScreenShowsFeedbackAfterAnswerSubmission() {
        val question = sampleQuestion()
        val scorer = QuizScorer()
        var state by mutableStateOf(QuizUiState(questions = listOf(question)))

        composeRule.setContent {
            StudyQuestTheme(highContrast = false, largeText = false) {
                QuizScreen(
                    uiState = state,
                    onReload = {},
                    onSelectAnswer = { answer -> state = state.copy(selectedAnswer = answer) },
                    onSubmitAnswer = {
                        state = state.copy(
                            answerResult = scorer.score(question, state.selectedAnswer ?: ""),
                            correctCount = 1,
                        )
                    },
                    onNextQuestion = {},
                    onReview = {},
                    onStats = {},
                )
            }
        }

        composeRule.onNodeWithText("Room").performClick()
        composeRule.onNodeWithText("Submit Answer").performClick()

        composeRule.onNodeWithText("Correct").assertIsDisplayed()
        composeRule.onNodeWithText("Finish Quiz").assertIsDisplayed()
    }

    private fun sampleQuestion(): Question = Question(
        id = 1,
        source = "seed",
        categoryId = "computers",
        categoryName = "Computer Basics",
        difficulty = Difficulty.Easy,
        questionText = "Which library stores local learning data?",
        answers = listOf("Room", "Retrofit", "Moshi", "OkHttp"),
        correctAnswer = "Room",
        explanation = "Room stores structured local data.",
    )
}
