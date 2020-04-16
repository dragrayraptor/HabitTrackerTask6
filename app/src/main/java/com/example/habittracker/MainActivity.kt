package com.example.habittracker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_pager.*

interface FabClickListener {
    fun onFabClick()
}

interface HabitSaveClickListener {
    fun onHabitSave(habit: Habit)
}

interface HabitClickListener {
    fun onHabitClick(habit: Habit, index: Int)
}

class MainActivity : AppCompatActivity(), FabClickListener, HabitSaveClickListener, HabitClickListener {
    private lateinit var appBarConfiguration: AppBarConfiguration

    private var viewPagerFragment = ViewPagerFragment()
    private val infoFragment = InfoFragment()
    private var habitAdditionFragment: HabitAdditionFragment? = null

    private var lastClickedHabit: Habit? = null
    private var lastClickedHabitIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        setSupportActionBar(toolbar)

        HabitsStorageModel.createDatabase(this)

        val drawerToggle =
            ActionBarDrawerToggle(
                this,
                drawer_layout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
        drawer_layout.addDrawerListener(drawerToggle)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content_navigation_fragment, ViewPagerFragment())
            .commit()

        nav_view.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.content_navigation_fragment, ViewPagerFragment())
                        .commit()
                }
                else -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.content_navigation_fragment, InfoFragment())
                        .commit()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    override fun onFabClick() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .replace(R.id.content_navigation_fragment, HabitAdditionFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onHabitSave(habit: Habit) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_navigation_fragment, ViewPagerFragment())
            .commit()
    }

    override fun onHabitClick(habit: Habit, index: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_navigation_fragment, HabitAdditionFragment.newInstance(habit))
            .addToBackStack(null)
            .commit()
    }
}