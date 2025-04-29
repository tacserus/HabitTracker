package com.example.habittracker.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.habittracker.domain.enums.HabitType
import com.example.habittracker.presentation.fragments.HabitsFragment

class HabitPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = listOf(
        HabitsFragment.newInstance(HabitType.GoodHabit),
        HabitsFragment.newInstance(HabitType.BadHabit)
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}