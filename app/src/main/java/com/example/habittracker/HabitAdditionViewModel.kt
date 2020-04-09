package com.example.habittracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HabitAdditionViewModel(private val model: HabitsStorageModel, private val currentHabit: Habit?): ViewModel() {
    private val mutableHabit: MutableLiveData<Habit?> = MutableLiveData()

    val habit: LiveData<Habit?> = mutableHabit

    init {
        load()
    }

    private fun load() {
        mutableHabit.postValue(currentHabit)
    }

    fun addHabit(habit: Habit) {
        if (currentHabit == null) {
            model.addHabit(habit)
        } else {
            model.changeHabit(currentHabit, habit)
        }
    }
}