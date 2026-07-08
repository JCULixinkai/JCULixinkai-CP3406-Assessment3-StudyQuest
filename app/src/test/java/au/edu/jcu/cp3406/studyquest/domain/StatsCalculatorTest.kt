package au.edu.jcu.cp3406.studyquest.domain

import au.edu.jcu.cp3406.studyquest.data.local.AttemptEntity
import au.edu.jcu.cp3406.studyquest.model.UserSettings
import com.google.common.truth.Truth.assertThat
import java.time.LocalDate
import java.time.ZoneId
import org.junit.Test

class StatsCalculatorTest {
    private val calculator = StatsCalculator(DailyGoalTracker())

    @Test
    fun `calculates accuracy weakest topic and daily progress`() {
        val now = System.currentTimeMillis()
        val attempts = listOf(
            attempt(categoryId = "maths", categoryName = "Mathematics", isCorrect = false, answeredAt = now),
            attempt(categoryId = "maths", categoryName = "Mathematics", isCorrect = true, answeredAt = now),
            attempt(categoryId = "computers", categoryName = "Computer Basics", isCorrect = true, answeredAt = now),
        )

        val stats = calculator.calculate(attempts, UserSettings(dailyGoal = 5))

        assertThat(stats.totalAnswered).isEqualTo(3)
        assertThat(stats.totalCorrect).isEqualTo(2)
        assertThat(stats.overallAccuracyPercent).isEqualTo(66)
        assertThat(stats.completedToday).isEqualTo(3)
        assertThat(stats.weakestTopic?.categoryId).isEqualTo("maths")
    }

    @Test
    fun `daily goal tracker counts consecutive active days`() {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now(zone).atStartOfDay(zone).toInstant().toEpochMilli()
        val yesterday = LocalDate.now(zone).minusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()
        val tracker = DailyGoalTracker()

        val streak = tracker.currentStreak(
            listOf(
                attempt(answeredAt = today),
                attempt(answeredAt = yesterday),
            ),
            zone,
        )

        assertThat(streak).isEqualTo(2)
    }

    private fun attempt(
        categoryId: String = "general",
        categoryName: String = "General Knowledge",
        isCorrect: Boolean = true,
        answeredAt: Long,
    ): AttemptEntity = AttemptEntity(
        questionId = answeredAt,
        questionText = "Question",
        categoryId = categoryId,
        categoryName = categoryName,
        selectedAnswer = "A",
        correctAnswer = "A",
        isCorrect = isCorrect,
        answeredAt = answeredAt,
        sessionId = 1,
        timeSpentSeconds = 4,
    )
}

