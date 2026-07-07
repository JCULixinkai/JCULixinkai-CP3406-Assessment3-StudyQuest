package au.edu.jcu.cp3406.studyquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import au.edu.jcu.cp3406.studyquest.ui.StudyQuestApp
import au.edu.jcu.cp3406.studyquest.ui.landing.LandingViewModel
import au.edu.jcu.cp3406.studyquest.ui.quiz.QuizViewModel
import au.edu.jcu.cp3406.studyquest.ui.review.ReviewViewModel
import au.edu.jcu.cp3406.studyquest.ui.settings.SettingsViewModel
import au.edu.jcu.cp3406.studyquest.ui.stats.StatsViewModel
import au.edu.jcu.cp3406.studyquest.ui.theme.StudyQuestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val landingViewModel: LandingViewModel by viewModels()
    private val quizViewModel: QuizViewModel by viewModels()
    private val reviewViewModel: ReviewViewModel by viewModels()
    private val statsViewModel: StatsViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settings = settingsViewModel.settings.collectAsStateWithLifecycle().value
            StudyQuestTheme(
                highContrast = settings.highContrast,
                largeText = settings.largeText,
            ) {
                StudyQuestApp(
                    landingViewModel = landingViewModel,
                    quizViewModel = quizViewModel,
                    reviewViewModel = reviewViewModel,
                    statsViewModel = statsViewModel,
                    settingsViewModel = settingsViewModel,
                )
            }
        }
    }
}

