package au.edu.jcu.cp3406.studyquest.data.remote

import au.edu.jcu.cp3406.studyquest.data.remote.dto.TriviaResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApiService {
    @GET("api.php")
    suspend fun fetchQuestions(
        @Query("amount") amount: Int = 10,
        @Query("category") category: Int,
        @Query("difficulty") difficulty: String?,
        @Query("type") type: String = "multiple",
        @Query("encode") encode: String = "url3986",
    ): TriviaResponseDto
}

