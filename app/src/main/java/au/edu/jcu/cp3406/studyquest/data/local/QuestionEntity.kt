package au.edu.jcu.cp3406.studyquest.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import au.edu.jcu.cp3406.studyquest.model.Difficulty
import au.edu.jcu.cp3406.studyquest.model.Question

@Entity(
    tableName = "questions",
    indices = [Index(value = ["normalizedQuestion"], unique = true)],
)
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val source: String,
    val sourceId: String,
    val categoryId: String,
    val categoryName: String,
    val difficulty: String,
    val questionText: String,
    val normalizedQuestion: String,
    val answers: List<String>,
    val correctAnswer: String,
    val explanation: String,
    val fetchedAt: Long,
) {
    fun toModel(): Question = Question(
        id = id,
        source = source,
        categoryId = categoryId,
        categoryName = categoryName,
        difficulty = Difficulty.entries.firstOrNull { it.name == difficulty } ?: Difficulty.Mixed,
        questionText = questionText,
        answers = answers,
        correctAnswer = correctAnswer,
        explanation = explanation,
    )
}

fun Question.toEntity(
    sourceId: String = normalizedQuestionKey(questionText),
    fetchedAt: Long = System.currentTimeMillis(),
): QuestionEntity = QuestionEntity(
    id = id,
    source = source,
    sourceId = sourceId,
    categoryId = categoryId,
    categoryName = categoryName,
    difficulty = difficulty.name,
    questionText = questionText,
    normalizedQuestion = normalizedQuestionKey(questionText),
    answers = answers,
    correctAnswer = correctAnswer,
    explanation = explanation,
    fetchedAt = fetchedAt,
)

fun normalizedQuestionKey(value: String): String = value.lowercase()
    .replace(Regex("[^a-z0-9]+"), " ")
    .trim()

