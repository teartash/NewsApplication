package com.andariadar.newsapplication.api
import com.andariadar.newsapplication.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Path

interface NewsApi {
    companion object {
        const val BASE_URL = "https://api.nytimes.com/"
        const val API_KEY = BuildConfig.API_KEY
    }

    @GET("svc/mostpopular/v2/viewed/{period}.json?api-key=$API_KEY")
    suspend fun newsOneDay(
        @Path("period") period: Int
    ): ResultList
}