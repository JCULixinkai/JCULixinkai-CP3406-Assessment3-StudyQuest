package au.edu.jcu.cp3406.studyquest.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import au.edu.jcu.cp3406.studyquest.model.DashboardStats
import au.edu.jcu.cp3406.studyquest.ui.landing.LandingScreen
import au.edu.jcu.cp3406.studyquest.ui.theme.StudyQuestTheme
import org.junit.Rule
import org.junit.Test

class LandingScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun landingScreenShowsRequiredEntryActions() {
        composeRule.setContent {
            StudyQuestTheme(highContrast = false, largeText = false) {
                LandingScreen(
                    stats = DashboardStats(),
                    onStartQuiz = {},
                    onReview = {},
                    onStats = {},
                )
            }
        }

        composeRule.onNodeWithText("CP3406-Assessment3-StudyQuest").assertIsDisplayed()
        composeRule.onNodeWithText("Start Quiz").assertIsDisplayed()
        composeRule.onNodeWithText("Review Mistakes").assertIsDisplayed()
    }
}
