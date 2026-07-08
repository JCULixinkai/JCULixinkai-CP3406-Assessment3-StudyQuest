package au.edu.jcu.cp3406.studyquest.domain

import au.edu.jcu.cp3406.studyquest.model.Difficulty
import au.edu.jcu.cp3406.studyquest.model.Question
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class QuizScorerTest {
    private val scorer = QuizScorer()

    @Test
    fun `scores correct answer and includes explanation`() {
        val result = scorer.score(sampleQuestion(), "Room")

        assertThat(result.isCorrect).isTrue()
        assertThat(result.explanation).contains("Correct")
        assertThat(result.explanation).contains("local records")
    }

    @Test
    fun `scores wrong answer and reveals correct answer`() {
        val result = scorer.score(sampleQuestion(), "Retrofit")

        assertThat(result.isCorrect).isFalse()
        assertThat(result.explanation).contains("Room")
    }

    private fun sampleQuestion(): Question = Question(
        id = 1,
        source = "seed",
        categoryId = "computers",
        categoryName = "Computer Basics",
        difficulty = Difficulty.Easy,
        questionText = "Which library stores local records?",
        answers = listOf("Room", "Retrofit", "Moshi", "OkHttp"),
        correctAnswer = "Room",
        explanation = "Room stores structured local records.",
    )
}

