package au.edu.jcu.cp3406.studyquest.data.repository

import au.edu.jcu.cp3406.studyquest.model.Difficulty
import au.edu.jcu.cp3406.studyquest.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<UserSettings>
    suspend fun currentSettings(): UserSettings
    suspend fun updateDailyGoal(goal: Int)
    suspend fun updateDifficulty(difficulty: Difficulty)
    suspend fun setCategoryEnabled(categoryId: String, enabled: Boolean)
    suspend fun updateLargeText(enabled: Boolean)
    suspend fun updateHighContrast(enabled: Boolean)
    suspend fun updateReduceMotion(enabled: Boolean)
    suspend fun updateInstantFeedback(enabled: Boolean)
}

