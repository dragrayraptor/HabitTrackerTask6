package com.example.habittracker

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.*
import java.io.Serializable
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.util.*

enum class Priority(val title: String) {
    Low("Низкий"), Medium("Средний"), High("Высокий")
}

enum class HabitType {
    Bad, Good
}

@Entity
@TypeConverters(HabitTypeConverter::class, HabitPriorityConverter::class)
data class Habit(@PrimaryKey val id: String, val title: String, val description: String, val priority: Priority,
            val type: HabitType, val count: Int, val periodicity: Int, val date: Int = 0): Serializable

class HabitWithoutId(val title: String, val description: String, val priority: Priority,
                     val type: HabitType, val count: Int, val periodicity: Int, val date: Int = 0): Serializable {
    fun getHabitWithId(id: String): Habit {
        return Habit(id, title, description, priority, type, count, periodicity, date + 1)
    }
}
