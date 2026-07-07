package au.edu.jcu.cp3406.studyquest.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.jcu.cp3406.studyquest.data.repository.ProgressRepository
import au.edu.jcu.cp3406.studyquest.model.ReviewQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val progressRepository: ProgressRepository,
) : ViewModel() {
    val reviewQuestions: StateFlow<List<ReviewQuestion>> = progressRepository.reviewQuestions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun markMastered(questionId: Long) = viewModelScope.launch {
        progressRepository.markMastered(questionId)
    }
}

