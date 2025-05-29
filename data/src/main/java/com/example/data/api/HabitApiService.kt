package com.example.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT

interface HabitApiService {
    @GET("habit")
    suspend fun getHabits(): Response<List<HabitDto>>

    @PUT("habit")
    suspend fun putHabit(
        @Body habit: HabitDto
    ): Response<HabitRequestUID>

    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    suspend fun deleteHabit(
        @Body uid: HabitRequestUID
    ): Response<Unit>

    @POST("habit_done")
    suspend fun addDoneMark(
        @Body habitDoneMark: HabitDoneMark
    ): Response<Unit>
}