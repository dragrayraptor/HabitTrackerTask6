package com.example.habittracker

import androidx.room.TypeConverter

class HabitTypeConverter {
    @TypeConverter
    fun fromType(type: HabitType): String = type.toString()

    @TypeConverter
    fun toType(value: String): HabitType = HabitType.valueOf(value)
}

class HabitPriorityConverter {
    @TypeConverter
    fun fromPriority(priority: Priority): String = priority.toString()

    @TypeConverter
    fun toPriority(value: String): Priority = Priority.valueOf(value)
}
