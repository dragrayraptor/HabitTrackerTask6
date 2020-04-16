package com.example.habittracker

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object HabitsStorageModel {
    private lateinit var db: AppDatabase

    fun createDatabase(context: Context) {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "HabitReader"
        ).allowMainThreadQueries().build()
    }

    fun getHabits(): List<Habit> {
        return db.habitDao().getAll()
    }

    fun addHabit(habit: Habit) {
        GlobalScope.launch(Dispatchers.Main) {
            db.habitDao().insert(habit)
        }
    }

    fun changeHabit(oldHabit: Habit, newHabit: Habit) {
        deleteHabit(oldHabit)
        addHabit(newHabit)
    }

    fun deleteHabit(habit: Habit) {
        GlobalScope.launch(Dispatchers.Main) {
            db.habitDao().delete(habit)
        }
    }

    fun getHabitsByType(type: HabitType): List<Habit> {
        val habits = getHabits()
        return when(type) {
            HabitType.Good -> habits.filter { it.type == HabitType.Good }
            HabitType.Bad -> habits.filter { it.type == HabitType.Bad }
        }
    }
}
