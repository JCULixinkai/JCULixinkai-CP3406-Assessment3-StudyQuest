# CP3406-Assessment3-StudyQuest

CP3406 Assessment 3 education app.

CP3406-Assessment3-StudyQuest is a learning quiz app for high school and university students. It helps students practise general knowledge, science, computer basics, mathematics, and history through short quizzes, mistake review, local progress statistics, and accessibility settings.

## Core Screens

- **Landing page**: daily goal progress, total answers, accuracy, streak, weakest topic, and shortcuts.
- **Quiz activity screen**: multiple-choice practice using live Open Trivia DB questions, cached questions, or offline fallback questions.
- **Review screen**: missed questions are saved for review and can be marked as mastered.
- **User statistics screen**: Room-backed total answers, accuracy, streak, daily progress, topic performance, and recent answers.
- **Settings screen**: difficulty, topics, daily goal, instant feedback, large text, high contrast, reduce motion, and local progress reset.

## Implementation

- Kotlin Android app using Jetpack Compose and Material 3.
- Navigation Component with five routed screens and bottom navigation.
- Hilt dependency injection for repositories, Room, Retrofit, OkHttp, and Moshi.
- Retrofit fetches questions from Open Trivia DB without an API key.
- Room stores cached questions, attempts, quiz sessions, review state, and settings.
- ViewModels expose immutable screen state and keep UI code separate from data access.
- Domain classes cover quiz scoring, topic statistics, daily streaks, and review priority.

## Ethical Design Decisions

- No login, real name, location, or cloud sync is used.
- Learning progress is stored locally in Room.
- Students can reset local progress from Settings.
- The app explains when cached or offline questions are used.
- Accessibility controls include large text, high contrast, and reduced motion.
- The app encourages learning progress without punitive streak or addictive pressure.

## Running The App

Open this folder in Android Studio and run the `app` configuration on an emulator or Android device.

From the terminal:

```bash
./gradlew assembleDebug
```

The debug APK is generated under:

```text
app/build/outputs/apk/debug/
```

## Verification

Run the main checks with:

```bash
./gradlew test
./gradlew compileDebugAndroidTestKotlin lintDebug assembleDebug
```

Covered tests include scoring, statistics, daily streaks, review priority, QuizViewModel behavior, and basic Compose screen checks.

## Submission Notes

For LearnJCU submission, export the Android project from Android Studio using `File > Export > Export to zip file...`, upload the self-reflection PDF, and provide the GitHub repository link.
