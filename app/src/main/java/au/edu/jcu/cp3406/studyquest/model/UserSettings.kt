package au.edu.jcu.cp3406.studyquest.model

data class UserSettings(
    val dailyGoal: Int = 10,
    val preferredDifficulty: Difficulty = Difficulty.Mixed,
    val enabledCategoryIds: List<String> = StudyCategories.all.map { it.id },
    val largeText: Boolean = false,
    val highContrast: Boolean = false,
    val reduceMotion: Boolean = false,
    val instantFeedback: Boolean = true,
)

