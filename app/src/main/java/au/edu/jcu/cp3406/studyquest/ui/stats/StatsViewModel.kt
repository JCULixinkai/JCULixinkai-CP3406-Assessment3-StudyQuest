package au.edu.jcu.cp3406.studyquest.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.jcu.cp3406.studyquest.data.repository.ProgressRepository
import au.edu.jcu.cp3406.studyquest.model.DashboardStats
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class StatsViewModel @Inject constructor(
    progressRepository: ProgressRepository,
) : ViewModel() {
    val stats: StateFlow<DashboardStats> = progressRepository.dashboardStats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardStats())
}

