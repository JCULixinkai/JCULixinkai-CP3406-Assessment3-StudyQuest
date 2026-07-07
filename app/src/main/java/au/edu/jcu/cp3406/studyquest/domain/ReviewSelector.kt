package au.edu.jcu.cp3406.studyquest.domain

import au.edu.jcu.cp3406.studyquest.model.ReviewQuestion
import javax.inject.Inject

class ReviewSelector @Inject constructor() {
    fun prioritize(items: List<ReviewQuestion>): List<ReviewQuestion> = items
        .filterNot { it.timesMissed <= 0 }
        .sortedWith(
            compareByDescending<ReviewQuestion> { it.timesMissed }
                .thenBy { it.lastReviewedAt },
        )
}

