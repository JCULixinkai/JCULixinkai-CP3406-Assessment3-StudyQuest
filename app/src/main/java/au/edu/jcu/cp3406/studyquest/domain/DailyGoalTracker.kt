package au.edu.jcu.cp3406.studyquest.domain

import au.edu.jcu.cp3406.studyquest.data.local.AttemptEntity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class DailyGoalTracker @Inject constructor() {
    fun completedToday(attempts: List<AttemptEntity>, zoneId: ZoneId = ZoneId.systemDefault()): Int {
        val today = LocalDate.now(zoneId)
        return attempts.count { it.answeredAt.toLocalDate(zoneId) == today }
    }

    fun currentStreak(attempts: List<AttemptEntity>, zoneId: ZoneId = ZoneId.systemDefault()): Int {
        val activeDays = attempts
            .map { it.answeredAt.toLocalDate(zoneId) }
            .toSet()
        if (activeDays.isEmpty()) return 0

        var cursor = LocalDate.now(zoneId)
        if (cursor !in activeDays) {
            cursor = cursor.minusDays(1)
        }

        var streak = 0
        while (cursor in activeDays) {
            streak += 1
            cursor = cursor.minusDays(1)
        }
        return streak
    }

    private fun Long.toLocalDate(zoneId: ZoneId): LocalDate =
        Instant.ofEpochMilli(this).atZone(zoneId).toLocalDate()
}

