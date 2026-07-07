package au.edu.jcu.cp3406.studyquest.model

enum class Difficulty(
    val label: String,
    val apiValue: String?,
) {
    Mixed("Mixed", null),
    Easy("Easy", "easy"),
    Medium("Medium", "medium"),
    Hard("Hard", "hard"),
}

