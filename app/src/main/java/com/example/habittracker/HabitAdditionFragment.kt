package com.example.habittracker

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar

import kotlinx.android.synthetic.main.activity_habit_addition.*
import kotlinx.android.synthetic.main.activity_habit_addition.habitDescription
import kotlinx.android.synthetic.main.activity_habit_addition.habitPriority
import kotlinx.android.synthetic.main.activity_habit_addition.habitTitle
import kotlinx.android.synthetic.main.activity_habit_addition.periodicity
import kotlinx.android.synthetic.main.activity_habit_addition.repetitionsCount
import java.lang.Exception

class HabitAdditionFragment : Fragment() {
    private lateinit var habitSaveClickListener: HabitSaveClickListener

    companion object {
        fun newInstance(habit: Habit) : HabitAdditionFragment {
            val fragment = HabitAdditionFragment()
            val bundle = Bundle()
            bundle.putSerializable(HABIT, habit)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        habitSaveClickListener = context as HabitSaveClickListener
    }

    lateinit var adapter: ArrayAdapter<String>
    private lateinit var viewModel: HabitAdditionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var currentHabit: Habit? = null
        arguments?.let {
            currentHabit = it.getSerializable(HABIT) as Habit?
        }
        viewModel = ViewModelProvider(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HabitAdditionViewModel(HabitsStorageModel, currentHabit) as T
            }
        }).get(HabitAdditionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.activity_habit_addition, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ArrayAdapter(
            activity!!.applicationContext,
            android.R.layout.simple_spinner_item,
            Priorities
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            habitPriority.adapter = adapter
        }
        habitPriority.setSelection(1)
        badHabit.isChecked = true

        habitPriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                parent?.setSelection(position)
            }

        }

        habitSaveButton.setOnClickListener {
            if (!isCorrectHabitInformation())
                return@setOnClickListener
            val habit = createHabit()
            viewModel.addHabit(habit)
            habitSaveClickListener.onHabitSave(habit)
        }

        viewModel.habit.observe(viewLifecycleOwner, Observer { habit ->
            if (habit != null) {
                fillHabit(habit)
            }
        })
    }

    private fun isCorrectHabitInformation(): Boolean {
        if (habitTitle.text.isEmpty() || repetitionsCount.text.isEmpty() || periodicity.text.isEmpty()) {
            Snackbar.make(habitSaveButton, getString(R.string.MessageIfHabitsFieldsAreEmpty), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            return false
        }
        return true
    }

    private fun fillHabit(habit: Habit) {
        habitTitle.setText(habit.title)
        habitDescription.setText(habit.description)
        habitPriority.setSelection(
            Priorities.indexOf(priorityToText[habit.priority]))
        when (habit.type) {
            HabitType.Good -> goodHabit.isChecked = true
            HabitType.Bad -> badHabit.isChecked = true
        }
        repetitionsCount.setText(habit.count.toString())
        periodicity.setText(habit.periodicity.toString())
    }

    private fun createHabit(): Habit {
        val title = habitTitle.text.toString()
        var description = habitDescription.text.toString()
        if (description == "")
            description = getString(R.string.NoDescriptionText)
        val priorityText = habitPriority.getItemAtPosition(habitPriority.selectedItemPosition).toString()
        val priority = when (priorityText) {
            Priorities[0] -> Priority.Low
            Priorities[1] -> Priority.Medium
            Priorities[2] -> Priority.High
            else -> throw Exception("Unknown priority text")
        }
        val habitType = when {
            goodHabit.isChecked -> HabitType.Good
            badHabit.isChecked -> HabitType.Bad
            else -> throw Exception("Habit type not checked")
        }
        val repetitionsCount = repetitionsCount.text.toString().toInt()
        val periodicity = periodicity.text.toString().toInt()
        return Habit(title, description, priority, habitType, repetitionsCount, periodicity)
    }
}
