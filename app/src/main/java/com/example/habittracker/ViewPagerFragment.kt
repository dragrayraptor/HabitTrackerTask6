package com.example.habittracker

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_pager.*

const val ADAPTER_POS = "ADAPTER_POS"

class HabitsPagerAdapter(
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {
    var goodHabits = ListFragment.newInstance("good_habits")
    var badHabits = ListFragment.newInstance("bad_habits")

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                goodHabits
            }
            else -> {
                badHabits
            }
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0)
            "Хорошие привычки"
        else
            "Вредные привычки"
    }
}

class ViewPagerFragment: Fragment() {
    private lateinit var fabClickListener: FabClickListener
    private lateinit var viewModel: ViewPagerViewModel
    private lateinit var adapter: HabitsPagerAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fabClickListener = context as FabClickListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ViewPagerViewModel(HabitsStorageModel) as T
            }
        }).get(ViewPagerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_pager, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = HabitsPagerAdapter(childFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        viewModel.goodHabits.observe(viewLifecycleOwner, Observer {
            if (it != null) adapter.goodHabits.setItems(it)
        })
        viewModel.badHabits.observe(viewLifecycleOwner, Observer {
            if (it != null) adapter.badHabits.setItems(it)
        })

        fab.setOnClickListener { view ->
            fabClickListener.onFabClick()
        }
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putInt(ADAPTER_POS, adapter.position)
//    }
//
//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        val position = savedInstanceState?.getInt(ADAPTER_POS, 0) ?: 0
//        adapter.
//    }

    override fun onResume() {
        super.onResume()
        if (context != null && view != null)
            hideKeyboardFrom(context!!, view!!)
    }

    fun hideKeyboardFrom(
        context: Context,
        view: View
    ) {
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}