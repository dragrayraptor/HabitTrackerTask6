package com.example.habittracker

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.internal.notifyAll
import okhttp3.internal.wait
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HabitsStorageModel {
    private lateinit var db: AppDatabase
    private lateinit var service: HabitsApi
    private val token = "76d3462e-5478-4ddd-8160-cf119cfd7613"

    fun createDatabase(context: Context) {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "HabitReader"
        ).allowMainThreadQueries().build()

        val gson = GsonBuilder()
            .registerTypeAdapter(Habit::class.java, HabitJsonSerializer())
            .registerTypeAdapter(Habit::class.java, HabitJsonDeserializer())
            .registerTypeAdapter(HabitWithoutId::class.java, HabitHabitWithoutIdJsonSerializer())
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://droid-test-server.doubletapp.ru/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        service = retrofit.create(HabitsApi::class.java)
        updateHabits()
    }

    private fun updateHabits() {
        GlobalScope.launch(Dispatchers.IO) {
            db.habitDao().deleteAll()
            val habits = service.getHabits(token)
            db.habitDao().insertAll(habits)
        }
    }

    fun getHabits(): LiveData<List<Habit>> {
        return db.habitDao().getAll()
    }

    fun addHabit(habitWithoutId: HabitWithoutId) {
        GlobalScope.launch(Dispatchers.IO) {
            val idString = service.putHabit(token, habitWithoutId).string()
            val json = JSONObject(idString)
            val id = json.getString("uid")
            val habit = habitWithoutId.getHabitWithId(id)
            db.habitDao().insert(habit)
        }
    }

    fun addHabit(habit: Habit) {
        GlobalScope.launch(Dispatchers.IO) {
            service.putHabit(token, habit).string()
            db.habitDao().insert(habit)
        }
    }

    fun changeHabit(oldHabit: Habit, newHabit: HabitWithoutId) {
        deleteHabit(oldHabit)
        val habit = newHabit.getHabitWithId(oldHabit.id)
        addHabit(habit)
    }

    fun deleteHabit(habit: Habit) {
        GlobalScope.launch(Dispatchers.IO) {
            db.habitDao().delete(habit)
        }
    }
}
