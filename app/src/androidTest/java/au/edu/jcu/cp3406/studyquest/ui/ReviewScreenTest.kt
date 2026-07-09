package au.edu.jcu.cp3406.studyquest.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import au.edu.jcu.cp3406.studyquest.model.Difficulty
import au.edu.jcu.cp3406.studyquest.model.Question
import au.edu.jcu.cp3406.studyquest.model.ReviewQuestion
import au.edu.jcu.cp3406.studyquest.ui.review.ReviewScreen
import au.edu.jcu.cp3406.studyquest.ui.theme.StudyQuestTheme
import org.junit.Rule
import org.junit.Test

class ReviewScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun reviewScreenShowsCorrectAnswerAndMasteryAction() {
        composeRule.setContent {
            StudyQuestTheme(highContrast = false, largeText = false) {
                ReviewScreen(
                    reviewQuestions = listOf(ReviewQuestion(sampleQuestion(), timesMissed = 2, lastReviewedAt = 100)),
                    onMarkMastered = {},
                    onStartQuiz = {},
                )
            }
        }

        composeRule.onNodeWithText("Correct answer: Room").assertIsDisplayed()
        composeRule.onNodeWithText("Missed 2 time(s)").assertIsDisplayed()
        composeRule.onNodeWithText("Mark as Mastered").assertIsDisplayed()
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

