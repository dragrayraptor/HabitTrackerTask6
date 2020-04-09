package com.example.habittracker

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable

enum class Priority {
    High, Medium, Low
}

enum class HabitType {
    Bad, Good
}

@Entity
@TypeConverters(HabitTypeConverter::class, HabitPriorityConverter::class)
data class Habit(@PrimaryKey val title: String, val description: String, val priority: Priority,
            val type: HabitType, val count: Int, val periodicity: Int): Serializable {
}
