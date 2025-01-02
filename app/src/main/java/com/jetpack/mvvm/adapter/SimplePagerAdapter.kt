package com.jetpack.mvvm.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class SimplePagerAdapter(fragmentActivity: FragmentActivity, private val data: List<Fragment>) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
