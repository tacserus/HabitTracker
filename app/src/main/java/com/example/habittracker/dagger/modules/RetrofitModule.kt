package com.example.habittracker.dagger.modules

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
    //private val TOKEN = BuildConfig.API_TOKEN

    @Singleton
    @Provides
    fun provideRemoteClient(): Retrofit {
        val interceptor = Interceptor { chain ->
            val originRequest = chain.request()
            val builder = originRequest.newBuilder().header(
                "Authorization",
                "TOKEN"
            )

            val newRequest = builder.build()
            chain.proceed(newRequest)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}