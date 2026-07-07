package au.edu.jcu.cp3406.studyquest.model

data class Question(
    val id: Long = 0,
    val source: String,
    val categoryId: String,
    val categoryName: String,
    val difficulty: Difficulty,
    val questionText: String,
    val answers: List<String>,
    val correctAnswer: String,
    val explanation: String,
)

