package au.edu.jcu.cp3406.studyquest.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import au.edu.jcu.cp3406.studyquest.model.DashboardStats
import au.edu.jcu.cp3406.studyquest.model.TopicPerformance
import au.edu.jcu.cp3406.studyquest.ui.stats.StatsScreen
import au.edu.jcu.cp3406.studyquest.ui.theme.StudyQuestTheme
import org.junit.Rule
import org.junit.Test

class StatsScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun statsScreenShowsTopicPerformanceAndDailyTarget() {
        composeRule.setContent {
            StudyQuestTheme(highContrast = false, largeText = false) {
                StatsScreen(
                    stats = DashboardStats(
                        totalAnswered = 5,
                        totalCorrect = 4,
                        overallAccuracyPercent = 80,
                        currentStreak = 2,
                        completedToday = 5,
                        dailyGoal = 20,
                        topicPerformance = listOf(
                            TopicPerformance(
                                categoryId = "computers",
                                categoryName = "Computer Basics",
                                attempts = 5,
                                correct = 4,
                            ),
                        ),
                    ),
                )
            }
        }

        composeRule.onNodeWithText("User Statistics").assertIsDisplayed()
        composeRule.onNodeWithText("5/20").assertIsDisplayed()
        composeRule.onNodeWithText("Computer Basics").assertIsDisplayed()
        composeRule.onNodeWithText("80% (4/5)").assertIsDisplayed()
    }
}
