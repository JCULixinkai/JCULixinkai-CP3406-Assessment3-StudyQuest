package au.edu.jcu.cp3406.studyquest.data.repository

import au.edu.jcu.cp3406.studyquest.data.local.SettingsDao
import au.edu.jcu.cp3406.studyquest.data.local.toEntity
import au.edu.jcu.cp3406.studyquest.model.Difficulty
import au.edu.jcu.cp3406.studyquest.model.StudyCategories
import au.edu.jcu.cp3406.studyquest.model.UserSettings
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultSettingsRepository @Inject constructor(
    private val settingsDao: SettingsDao,
) : SettingsRepository {
    override val settings: Flow<UserSettings> = settingsDao.observeSettings()
        .map { it?.toModel() ?: UserSettings() }

    override suspend fun currentSettings(): UserSettings =
        settingsDao.getSettings()?.toModel() ?: UserSettings()

    override suspend fun updateDailyGoal(goal: Int) {
        save(currentSettings().copy(dailyGoal = goal.coerceIn(5, 20)))
    }

    override suspend fun updateDifficulty(difficulty: Difficulty) {
        save(currentSettings().copy(preferredDifficulty = difficulty))
    }

    override suspend fun setCategoryEnabled(categoryId: String, enabled: Boolean) {
        val current = currentSettings()
        val next = if (enabled) {
            (current.enabledCategoryIds + categoryId).distinct()
        } else {
            current.enabledCategoryIds - categoryId
        }.ifEmpty { StudyCategories.all.map { it.id } }
        save(current.copy(enabledCategoryIds = next))
    }

    override suspend fun updateLargeText(enabled: Boolean) {
        save(currentSettings().copy(largeText = enabled))
    }

    override suspend fun updateHighContrast(enabled: Boolean) {
        save(currentSettings().copy(highContrast = enabled))
    }

    override suspend fun updateReduceMotion(enabled: Boolean) {
        save(currentSettings().copy(reduceMotion = enabled))
    }

    override suspend fun updateInstantFeedback(enabled: Boolean) {
        save(currentSettings().copy(instantFeedback = enabled))
    }

    private suspend fun save(settings: UserSettings) {
        settingsDao.saveSettings(settings.toEntity())
    }
}

