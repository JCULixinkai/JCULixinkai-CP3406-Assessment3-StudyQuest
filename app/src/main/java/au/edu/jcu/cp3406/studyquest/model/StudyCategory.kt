package au.edu.jcu.cp3406.studyquest.model

data class StudyCategory(
    val id: String,
    val label: String,
    val apiCategory: Int,
)

object StudyCategories {
    val all = listOf(
        StudyCategory("general", "General Knowledge", 9),
        StudyCategory("science", "Science & Nature", 17),
        StudyCategory("computers", "Computer Basics", 18),
        StudyCategory("maths", "Mathematics", 19),
        StudyCategory("history", "History", 23),
    )

    fun byId(id: String): StudyCategory = all.firstOrNull { it.id == id } ?: all.first()
}

