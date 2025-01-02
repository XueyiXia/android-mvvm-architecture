package com.jetpack.mvvm.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.framework.mvvm.base.BaseMvvmActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jetpack.mvvm.BR
import com.jetpack.mvvm.R
import com.jetpack.mvvm.adapter.SimplePagerAdapter
import com.jetpack.mvvm.adapter.SimpleRecyclerAdapter
import com.jetpack.mvvm.databinding.ActivityNestedViewPagerBinding
import com.jetpack.mvvm.fragment.LinearFragment
import com.jetpack.mvvm.fragment.RecyclerViewFragment
import com.jetpack.mvvm.fragment.ScrollViewFragment
import com.jetpack.mvvm.viewmodel.TouchViewModel


/**
 * @author:
 * @date: 2023-06-06
 * @time: 16:00
 * @说明:
 */
class TouchActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_view_pager)

        initView()
    }

    fun initView() {


        val topRecyclerView:RecyclerView=findViewById(R.id.topRecyclerView)
        topRecyclerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        val bannerAdapter = SimpleRecyclerAdapter(this,getBanner())
        topRecyclerView.adapter=bannerAdapter

//        PagerSnapHelper().attachToRecyclerView(mBinding.comboTopView)


        val tabLayout: TabLayout =findViewById(R.id.tabLayout)
        val viewPager:ViewPager2=findViewById(R.id.viewPager)
        val pagerAdapter = SimplePagerAdapter(this, getPageFragments())
        viewPager.setAdapter(pagerAdapter)

        val labels = arrayOf("linear", "scroll", "recycler")
        TabLayoutMediator(
            tabLayout,
            viewPager
        ) { tab, position ->
            tab.setText(labels[position])
        }.attach()
    }

    private fun getBanner(): MutableList<String> {
        val data: MutableList<String> = ArrayList()
        data.add("#22dd99")
        data.add("#546e7a")
        data.add("#263238")
        return data
    }

    private fun getPageFragments(): List<Fragment> {
        val data: MutableList<Fragment> = ArrayList<Fragment>()
        data.add(LinearFragment())
        data.add(ScrollViewFragment())
        data.add(RecyclerViewFragment())
        return data
    }
}