package au.edu.jcu.cp3406.studyquest.domain

import au.edu.jcu.cp3406.studyquest.model.StudyCategories
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SeedQuestionsTest {
    @Test
    fun `offline questions cover each study category`() {
        val categoryIds = SeedQuestions.all().map { it.categoryId }.toSet()

        assertThat(categoryIds).containsAtLeastElementsIn(StudyCategories.all.map { it.id })
    }

    @Test
    fun `offline questions provide a useful fallback bank`() {
        assertThat(SeedQuestions.all()).hasSize(12)
        assertThat(SeedQuestions.all().all { question ->
            question.answers.size == 4 && question.correctAnswer in question.answers
        }).isTrue()
    }
}
