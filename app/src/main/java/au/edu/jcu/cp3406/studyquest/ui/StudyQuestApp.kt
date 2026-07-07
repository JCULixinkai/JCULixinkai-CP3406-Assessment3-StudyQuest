package au.edu.jcu.cp3406.studyquest.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import au.edu.jcu.cp3406.studyquest.ui.landing.LandingScreen
import au.edu.jcu.cp3406.studyquest.ui.landing.LandingViewModel
import au.edu.jcu.cp3406.studyquest.ui.quiz.QuizScreen
import au.edu.jcu.cp3406.studyquest.ui.quiz.QuizViewModel
import au.edu.jcu.cp3406.studyquest.ui.review.ReviewScreen
import au.edu.jcu.cp3406.studyquest.ui.review.ReviewViewModel
import au.edu.jcu.cp3406.studyquest.ui.settings.SettingsScreen
import au.edu.jcu.cp3406.studyquest.ui.settings.SettingsViewModel
import au.edu.jcu.cp3406.studyquest.ui.stats.StatsScreen
import au.edu.jcu.cp3406.studyquest.ui.stats.StatsViewModel

enum class StudyQuestDestination(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
) {
    Landing("landing", "Home", Icons.Filled.Home),
    Quiz("quiz", "Quiz", Icons.Filled.Quiz),
    Review("review", "Review", Icons.Filled.Replay),
    Stats("stats", "Stats", Icons.Filled.BarChart),
    Settings("settings", "Settings", Icons.Filled.Settings),
}

@Composable
fun StudyQuestApp(
    landingViewModel: LandingViewModel,
    quizViewModel: QuizViewModel,
    reviewViewModel: ReviewViewModel,
    statsViewModel: StatsViewModel,
    settingsViewModel: SettingsViewModel,
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: StudyQuestDestination.Landing.route

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                StudyQuestDestination.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = currentRoute == destination.route,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = StudyQuestDestination.Landing.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(StudyQuestDestination.Landing.route) {
                LandingScreen(
                    stats = landingViewModel.stats.collectAsStateWithLifecycle().value,
                    onStartQuiz = { navController.navigate(StudyQuestDestination.Quiz.route) },
                    onReview = { navController.navigate(StudyQuestDestination.Review.route) },
                    onStats = { navController.navigate(StudyQuestDestination.Stats.route) },
                )
            }
            composable(StudyQuestDestination.Quiz.route) {
                QuizScreen(
                    uiState = quizViewModel.uiState.collectAsStateWithLifecycle().value,
                    onReload = quizViewModel::loadQuiz,
                    onSelectAnswer = quizViewModel::selectAnswer,
                    onSubmitAnswer = quizViewModel::submitAnswer,
                    onNextQuestion = quizViewModel::nextQuestion,
                    onReview = { navController.navigate(StudyQuestDestination.Review.route) },
                    onStats = { navController.navigate(StudyQuestDestination.Stats.route) },
                )
            }
            composable(StudyQuestDestination.Review.route) {
                ReviewScreen(
                    reviewQuestions = reviewViewModel.reviewQuestions.collectAsStateWithLifecycle().value,
                    onMarkMastered = reviewViewModel::markMastered,
                    onStartQuiz = { navController.navigate(StudyQuestDestination.Quiz.route) },
                )
            }
            composable(StudyQuestDestination.Stats.route) {
                StatsScreen(stats = statsViewModel.stats.collectAsStateWithLifecycle().value)
            }
            composable(StudyQuestDestination.Settings.route) {
                SettingsScreen(
                    settings = settingsViewModel.settings.collectAsStateWithLifecycle().value,
                    onDailyGoalChange = settingsViewModel::setDailyGoal,
                    onDifficultyChange = settingsViewModel::setDifficulty,
                    onCategoryToggle = settingsViewModel::setCategoryEnabled,
                    onLargeTextChange = settingsViewModel::setLargeText,
                    onHighContrastChange = settingsViewModel::setHighContrast,
                    onReduceMotionChange = settingsViewModel::setReduceMotion,
                    onInstantFeedbackChange = settingsViewModel::setInstantFeedback,
                    onResetProgress = settingsViewModel::resetProgress,
                )
            }
        }
    }
}

