package com.example.habittracker.dagger

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RepeatRequestInterceptor(
    private val maxRepeatCount: Int,
    private val repeatDelay: Long
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response? = null
        var exception: IOException? = null
        var tryCount = 0

        while (tryCount < maxRepeatCount && (response == null || !response.isSuccessful)) {
            response?.close()

            try {
                response = chain.proceed(request)
                if (response.isSuccessful) {
                    return response
                }
            } catch (e: IOException) {
                exception = e
            }

            tryCount++
            if (tryCount < maxRepeatCount && (response == null || !response.isSuccessful)) {
                try {
                    Thread.sleep(repeatDelay)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    throw exception ?: IOException("Retry interrupted", e)
                }
            }
        }

        return response ?: throw exception ?: IOException("All retries failed for ${request.url}")
    }
}