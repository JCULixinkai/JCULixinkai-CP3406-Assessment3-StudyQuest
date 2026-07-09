package au.edu.jcu.cp3406.studyquest.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

private val StandardLight = lightColorScheme(
    primary = Color(0xFF2E6F57),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD7F0E2),
    onPrimaryContainer = Color(0xFF092115),
    secondary = Color(0xFF5B6EAE),
    secondaryContainer = Color(0xFFE1E6FF),
    background = Color(0xFFF7FAF7),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFE2EAE4),
    error = Color(0xFFBA1A1A),
)

private val HighContrastLight = lightColorScheme(
    primary = Color(0xFF003C2A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB8F2D1),
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFF20245F),
    background = Color.White,
    surface = Color.White,
    surfaceVariant = Color(0xFFE8E8E8),
    outline = Color.Black,
)

@Composable
fun StudyQuestTheme(
    highContrast: Boolean,
    largeText: Boolean,
    content: @Composable () -> Unit,
) {
    val typography = if (largeText) largeTypography() else Typography()
    MaterialTheme(
        colorScheme = when {
            highContrast -> HighContrastLight
            else -> StandardLight
        },
        typography = typography,
        content = content,
    )
}

private fun largeTypography(): Typography {
    val base = Typography()
    return base.copy(
        bodySmall = base.bodySmall.scale(1.14f),
        bodyMedium = base.bodyMedium.scale(1.14f),
        bodyLarge = base.bodyLarge.scale(1.14f),
        titleSmall = base.titleSmall.scale(1.1f),
        titleMedium = base.titleMedium.scale(1.1f),
        titleLarge = base.titleLarge.scale(1.1f),
        headlineSmall = base.headlineSmall.scale(1.08f),
        headlineMedium = base.headlineMedium.scale(1.08f),
    )
}

private fun TextStyle.scale(multiplier: Float): TextStyle =
    copy(fontSize = (fontSize.value * multiplier).sp)
