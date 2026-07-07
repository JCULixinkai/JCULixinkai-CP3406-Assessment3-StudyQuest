package au.edu.jcu.cp3406.studyquest.data.remote.dto

import com.squareup.moshi.Json

data class TriviaResponseDto(
    @param:Json(name = "response_code") val responseCode: Int,
    val results: List<TriviaQuestionDto> = emptyList(),
)

data class TriviaQuestionDto(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    @param:Json(name = "correct_answer") val correctAnswer: String,
    @param:Json(name = "incorrect_answers") val incorrectAnswers: List<String>,
)
