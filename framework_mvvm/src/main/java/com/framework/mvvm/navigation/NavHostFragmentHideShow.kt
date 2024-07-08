package com.framework.mvvm.navigation

import android.view.View
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment


/**
 * @author: xiaxueyi
 * @date: 2023-05-29
 * @time: 10:20
 * @说明: Hide - Show NavHostFragment
 */
class NavHostFragmentHideShow : NavHostFragment() {
    @Deprecated("Use {@link #onCreateNavController(NavController)}")
    override fun createFragmentNavigator(): Navigator<out FragmentNavigator.Destination> {
        return FragmentNavigatorHideShow(requireContext(), childFragmentManager, containerId)
    }

    /**
     * @return 使用自己的FragmentNavigator
     */
//    override fun createFragmentNavigator(): FragmentNavigator.Destination< Navigator<out FragmentNavigator.Destination>> {
//        return FragmentNavigatorHideShow(requireContext(), childFragmentManager, containerId)
//    }


    private val containerId: Int
        get() {
            val id = id
            return if (id != 0 && id != View.NO_ID) {
                id
                // Fallback to using our own ID if this Fragment wasn't added via
                // add(containerViewId, Fragment)
            } else androidx.navigation.fragment.R.id.nav_host_fragment_container
        }
}