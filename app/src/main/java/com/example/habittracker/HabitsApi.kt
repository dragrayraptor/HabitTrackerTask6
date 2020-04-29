package com.example.habittracker

import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface HabitsApi {
    @GET("habit")
    suspend fun getHabits(@Header("Authorization") authorization: String): List<Habit>

    @PUT("habit")
    suspend fun putHabit(@Header("Authorization") authorization: String, @Body habit: Habit): ResponseBody

    @PUT("habit")
    suspend fun putHabit(@Header("Authorization") authorization: String, @Body habitWithoutId: HabitWithoutId): ResponseBody
}