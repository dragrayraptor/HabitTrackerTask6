package com.example.habittracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewPagerViewModel(private val model: HabitsStorageModel) : ViewModel() {
    private val mutableGoodHabits: MutableLiveData<List<Habit>?> = MutableLiveData()
    private val mutableBadHabits: MutableLiveData<List<Habit>?> = MutableLiveData()

    val goodHabits: LiveData<List<Habit>?> = mutableGoodHabits
    val badHabits: LiveData<List<Habit>?> = mutableBadHabits

    init {
        load()
    }

    private fun load() {
        val habitsData = model.getHabits()
        habitsData.observeForever {
            mutableGoodHabits.postValue(
                getHabitsByType(it, HabitType.Good))
            mutableBadHabits.postValue(
                getHabitsByType(it, HabitType.Bad))
        }
    }

    fun getHabitsByType(habits: List<Habit>, type: HabitType): List<Habit> {
        return when(type) {
            HabitType.Good -> habits.filter { it.type == HabitType.Good }
            HabitType.Bad -> habits.filter { it.type == HabitType.Bad }
        }
    }
}