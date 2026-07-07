package au.edu.jcu.cp3406.studyquest.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.jcu.cp3406.studyquest.data.repository.ProgressRepository
import au.edu.jcu.cp3406.studyquest.data.repository.SettingsRepository
import au.edu.jcu.cp3406.studyquest.model.Difficulty
import au.edu.jcu.cp3406.studyquest.model.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val progressRepository: ProgressRepository,
) : ViewModel() {
    val settings: StateFlow<UserSettings> = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UserSettings())

    fun setDailyGoal(goal: Int) = viewModelScope.launch {
        settingsRepository.updateDailyGoal(goal)
    }

    fun setDifficulty(difficulty: Difficulty) = viewModelScope.launch {
        settingsRepository.updateDifficulty(difficulty)
    }

    fun setCategoryEnabled(categoryId: String, enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setCategoryEnabled(categoryId, enabled)
    }

    fun setLargeText(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.updateLargeText(enabled)
    }

    fun setHighContrast(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.updateHighContrast(enabled)
    }

    fun setReduceMotion(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.updateReduceMotion(enabled)
    }

    fun setInstantFeedback(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.updateInstantFeedback(enabled)
    }

    fun resetProgress() = viewModelScope.launch {
        progressRepository.clearProgress()
    }
}

