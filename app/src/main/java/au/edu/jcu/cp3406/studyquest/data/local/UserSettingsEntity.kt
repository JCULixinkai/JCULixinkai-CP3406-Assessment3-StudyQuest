package au.edu.jcu.cp3406.studyquest.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import au.edu.jcu.cp3406.studyquest.model.Difficulty
import au.edu.jcu.cp3406.studyquest.model.UserSettings

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey val id: Int = SETTINGS_ID,
    val dailyGoal: Int,
    val preferredDifficulty: String,
    val enabledCategoryIds: List<String>,
    val largeText: Boolean,
    val highContrast: Boolean,
    val reduceMotion: Boolean,
    val instantFeedback: Boolean,
) {
    fun toModel(): UserSettings = UserSettings(
        dailyGoal = dailyGoal,
        preferredDifficulty = Difficulty.entries.firstOrNull { it.name == preferredDifficulty } ?: Difficulty.Mixed,
        enabledCategoryIds = enabledCategoryIds,
        largeText = largeText,
        highContrast = highContrast,
        reduceMotion = reduceMotion,
        instantFeedback = instantFeedback,
    )

    companion object {
        const val SETTINGS_ID = 1
    }
}

fun UserSettings.toEntity(): UserSettingsEntity = UserSettingsEntity(
    dailyGoal = dailyGoal,
    preferredDifficulty = preferredDifficulty.name,
    enabledCategoryIds = enabledCategoryIds,
    largeText = largeText,
    highContrast = highContrast,
    reduceMotion = reduceMotion,
    instantFeedback = instantFeedback,
)

