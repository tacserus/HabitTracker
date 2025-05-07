package com.example.habittracker.dagger.modules

import com.example.habittracker.BuildConfig
import com.example.habittracker.data.api.HabitApiService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {
    private val BASE_URL = "https://droid-test-server.doubletapp.ru/api/"
    private val TOKEN = BuildConfig.API_TOKEN

    @Provides
    fun provideInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originRequest = chain.request()
            val builder = originRequest.newBuilder().header(
                "Authorization",
                TOKEN
            )

            val newRequest = builder.build()
            chain.proceed(newRequest)
        }
    }

    @Provides
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .addInterceptor(interceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRemoteClient(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideHabitApiService(retrofit: Retrofit): HabitApiService {
        return retrofit.create(HabitApiService::class.java)
    }
}