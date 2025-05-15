package com.example.data.dagger.modules

import com.example.data.BuildConfig
import com.example.data.api.HabitApiService
import com.example.data.api.RepeatRequestInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class RetrofitModule {
    companion object {
        const val AUTH_INTERCEPTOR = "AuthInterceptor"
        const val REPEAT_INTERCEPTOR = "RepeatInterceptor"
        const val BASE_URL = "https://droid-test-server.doubletapp.ru/api/"
        const val TOKEN = BuildConfig.API_TOKEN
        const val MAX_REPEAT_COUNT = 4
        const val REPEAT_DELAY = 1000L
    }


    @Provides
    @Singleton
    @Named(REPEAT_INTERCEPTOR)
    fun provideRepeatRequestInterceptor(): Interceptor {
        return RepeatRequestInterceptor(
            maxRepeatCount = MAX_REPEAT_COUNT,
            repeatDelay = REPEAT_DELAY
        )
    }

    @Provides
    @Singleton
    @Named(AUTH_INTERCEPTOR)
    fun provideAuthorizationInterceptor(): Interceptor {
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
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @Named(AUTH_INTERCEPTOR) authorizationInterceptor: Interceptor,
        @Named(REPEAT_INTERCEPTOR) repeatRequestInterceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(repeatRequestInterceptor)
            .addInterceptor(authorizationInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRemoteClient(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideHabitApiService(retrofit: Retrofit): HabitApiService {
        return retrofit.create(HabitApiService::class.java)
    }
}