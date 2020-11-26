package com.andariadar.newsapplication.di

import android.app.Application
import androidx.room.Room
import com.andariadar.newsapplication.constants.Constants.Companion.DATABASE_NAME
import com.andariadar.newsapplication.db.NewsDatabase
import com.andariadar.newsapplication.model.api.NewsApi
import com.andariadar.newsapplication.model.dao.NewsDao
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()

        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(NewsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(client)
            .build()

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApi =
        retrofit.create(NewsApi::class.java)

    @Singleton
    @Provides
    fun provideNewsDataBase(app: Application): NewsDatabase {
        return Room.databaseBuilder(app, NewsDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsDao(db: NewsDatabase): NewsDao {
        return db.newsDao()
    }
}