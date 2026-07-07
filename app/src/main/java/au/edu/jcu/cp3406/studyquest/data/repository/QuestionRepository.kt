package au.edu.jcu.cp3406.studyquest.data.repository

import au.edu.jcu.cp3406.studyquest.model.Difficulty
import au.edu.jcu.cp3406.studyquest.model.Question

interface QuestionRepository {
    suspend fun loadQuestions(
        categoryIds: List<String>,
        difficulty: Difficulty,
        amount: Int,
    ): QuestionLoadResult

    suspend fun findQuestion(id: Long): Question?
}

sealed interface QuestionLoadResult {
    data class Success(
        val questions: List<Question>,
        val message: String? = null,
    ) : QuestionLoadResult

    data class Failure(val message: String) : QuestionLoadResult
}

