package au.edu.jcu.cp3406.studyquest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        QuestionEntity::class,
        AttemptEntity::class,
        QuizSessionEntity::class,
        UserSettingsEntity::class,
        ReviewStateEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class StudyQuestDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun attemptDao(): AttemptDao
    abstract fun quizSessionDao(): QuizSessionDao
    abstract fun settingsDao(): SettingsDao
    abstract fun reviewStateDao(): ReviewStateDao
}
