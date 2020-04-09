package com.example.habittracker

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit")
    fun getAll(): List<Habit>

    @Query("SELECT * FROM habit WHERE title LIKE :title LIMIT 1")
    fun findByTitle(title: String): Habit

    @Insert
    fun insert(habit: Habit)

    @Delete
    fun delete(habit: Habit)
}