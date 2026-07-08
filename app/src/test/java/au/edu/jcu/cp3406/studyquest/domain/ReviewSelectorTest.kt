package au.edu.jcu.cp3406.studyquest.domain

import au.edu.jcu.cp3406.studyquest.model.Difficulty
import au.edu.jcu.cp3406.studyquest.model.Question
import au.edu.jcu.cp3406.studyquest.model.ReviewQuestion
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ReviewSelectorTest {
    @Test
    fun `prioritizes most missed review questions first`() {
        val selector = ReviewSelector()
        val items = listOf(
            ReviewQuestion(sampleQuestion(1), timesMissed = 1, lastReviewedAt = 100),
            ReviewQuestion(sampleQuestion(2), timesMissed = 3, lastReviewedAt = 200),
            ReviewQuestion(sampleQuestion(3), timesMissed = 0, lastReviewedAt = 300),
        )

        val result = selector.prioritize(items)

        assertThat(result.map { it.question.id }).containsExactly(2L, 1L).inOrder()
    }

    private fun sampleQuestion(id: Long): Question = Question(
        id = id,
        source = "seed",
        categoryId = "general",
        categoryName = "General Knowledge",
        difficulty = Difficulty.Easy,
        questionText = "Question $id",
        answers = listOf("A", "B", "C", "D"),
        correctAnswer = "A",
        explanation = "Explanation",
    )
}

