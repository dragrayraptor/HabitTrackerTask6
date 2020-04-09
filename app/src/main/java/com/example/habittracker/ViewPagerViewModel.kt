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
        mutableGoodHabits.postValue(
            model.getHabitsByType(HabitType.Good))
        mutableBadHabits.postValue(
            model.getHabitsByType(HabitType.Bad))
    }
}