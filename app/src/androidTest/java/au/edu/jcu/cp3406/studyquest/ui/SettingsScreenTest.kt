package au.edu.jcu.cp3406.studyquest.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import au.edu.jcu.cp3406.studyquest.model.UserSettings
import au.edu.jcu.cp3406.studyquest.ui.settings.SettingsScreen
import au.edu.jcu.cp3406.studyquest.ui.theme.StudyQuestTheme
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun settingsScreenShowsAccessibilityAndDataControls() {
        composeRule.setContent {
            StudyQuestTheme(highContrast = false, largeText = false) {
                SettingsScreen(
                    settings = UserSettings(),
                    onDailyGoalChange = {},
                    onDifficultyChange = {},
                    onCategoryToggle = { _, _ -> },
                    onLargeTextChange = {},
                    onHighContrastChange = {},
                    onReduceMotionChange = {},
                    onInstantFeedbackChange = {},
                    onResetProgress = {},
                )
            }
        }

        composeRule.onNodeWithText("Large text mode").assertIsDisplayed()
        composeRule.onNodeWithText("High contrast mode").assertIsDisplayed()
        composeRule.onNodeWithText("Reset Progress").assertIsDisplayed()
    }
}
