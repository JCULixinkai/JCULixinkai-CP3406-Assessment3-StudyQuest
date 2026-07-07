package au.edu.jcu.cp3406.studyquest.di

import android.content.Context
import androidx.room.Room
import au.edu.jcu.cp3406.studyquest.data.local.AttemptDao
import au.edu.jcu.cp3406.studyquest.data.local.QuestionDao
import au.edu.jcu.cp3406.studyquest.data.local.QuizSessionDao
import au.edu.jcu.cp3406.studyquest.data.local.ReviewStateDao
import au.edu.jcu.cp3406.studyquest.data.local.SettingsDao
import au.edu.jcu.cp3406.studyquest.data.local.StudyQuestDatabase
import au.edu.jcu.cp3406.studyquest.data.remote.TriviaApiService
import au.edu.jcu.cp3406.studyquest.data.repository.DefaultProgressRepository
import au.edu.jcu.cp3406.studyquest.data.repository.DefaultQuestionRepository
import au.edu.jcu.cp3406.studyquest.data.repository.DefaultSettingsRepository
import au.edu.jcu.cp3406.studyquest.data.repository.ProgressRepository
import au.edu.jcu.cp3406.studyquest.data.repository.QuestionRepository
import au.edu.jcu.cp3406.studyquest.data.repository.SettingsRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindings {
    @Binds
    @Singleton
    abstract fun bindQuestionRepository(repository: DefaultQuestionRepository): QuestionRepository

    @Binds
    @Singleton
    abstract fun bindProgressRepository(repository: DefaultProgressRepository): ProgressRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(repository: DefaultSettingsRepository): SettingsRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): StudyQuestDatabase = Room.databaseBuilder(
        context,
        StudyQuestDatabase::class.java,
        "studyquest.db",
    ).fallbackToDestructiveMigration(false).build()

    @Provides
    fun provideQuestionDao(database: StudyQuestDatabase): QuestionDao = database.questionDao()

    @Provides
    fun provideAttemptDao(database: StudyQuestDatabase): AttemptDao = database.attemptDao()

    @Provides
    fun provideQuizSessionDao(database: StudyQuestDatabase): QuizSessionDao = database.quizSessionDao()

    @Provides
    fun provideSettingsDao(database: StudyQuestDatabase): SettingsDao = database.settingsDao()

    @Provides
    fun provideReviewStateDao(database: StudyQuestDatabase): ReviewStateDao = database.reviewStateDao()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://opentdb.com/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideTriviaApiService(retrofit: Retrofit): TriviaApiService =
        retrofit.create(TriviaApiService::class.java)
}

