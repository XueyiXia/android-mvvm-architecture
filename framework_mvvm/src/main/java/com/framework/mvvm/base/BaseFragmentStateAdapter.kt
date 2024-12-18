package com.framework.mvvm.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @author: xiaxueyi
 * @date: 2022-09-20
 * @time: 14:56
 * @说明:
 */

class BaseFragmentStateAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private var fragmentList: List<Fragment> = emptyList()
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    override fun getItemId(position: Int): Long = fragmentList[position].hashCode().toLong()

    override fun containsItem(itemId: Long): Boolean = fragmentList.any { it.hashCode().toLong() == itemId }


}