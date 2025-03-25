package com.jetpack.mvvm.activities

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.framework.mvvm.base.BaseActivityNew
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jetpack.mvvm.R
import com.jetpack.mvvm.adapter.SimplePagerAdapter
import com.jetpack.mvvm.adapter.SimpleRecyclerAdapter
import com.jetpack.mvvm.databinding.ActivityCudaBinding
import com.jetpack.mvvm.databinding.ActivityTouchBinding
import com.jetpack.mvvm.fragment.LinearFragment
import com.jetpack.mvvm.fragment.RecyclerViewFragment
import com.jetpack.mvvm.fragment.ScrollViewFragment
import com.jetpack.mvvm.fragment.WebViewFragment


/**
 * @author:
 * @date: 2023-06-06
 * @time: 16:00
 * @说明:
 */
class TouchActivity: BaseActivityNew<ActivityCudaBinding>(){

    override fun bindDataBinding(): ActivityCudaBinding {
        return ActivityCudaBinding.inflate(layoutInflater)
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        initView1()
    }

    fun initView1() {


        mBinding.topRecyclerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        val bannerAdapter = SimpleRecyclerAdapter(this,getBanner())
        mBinding.topRecyclerView.adapter=bannerAdapter
//
//        PagerSnapHelper().attachToRecyclerView(mBinding.comboTopView)
//
//
//
//        val pagerAdapter = SimplePagerAdapter(this, getPageFragments())
//        mBinding.topRecyclerView.setAdapter(pagerAdapter)
//
//        val labels = arrayOf("linear", "scroll", "recycler", "webView")
//        TabLayoutMediator(
//            tabLayout,
//            viewPager
//        ) { tab, position ->
//            tab.setText(labels[position])
//        }.attach()
    }

    private fun getBanner(): MutableList<String> {
        val data: MutableList<String> = ArrayList()
        data.add("#22dd99")
        data.add("#546e7a")
        data.add("#263238")
        data.add("#22cd99")
        data.add("#546e75")
        data.add("#255fff")
        data.add("#2ffd55")
        data.add("#546e75")
        data.add("#255fff")
        data.add("#22dd99")
        data.add("#546e7a")
        data.add("#263238")
        data.add("#22cd99")
        data.add("#546e75")
        data.add("#255fff")
        data.add("#2ffd55")
        data.add("#546e75")
        data.add("#255fff")
        return data
    }

    private fun getPageFragments(): List<Fragment> {
        val data: MutableList<Fragment> = ArrayList<Fragment>()
        data.add(LinearFragment())
        data.add(ScrollViewFragment())
        data.add(RecyclerViewFragment())
        data.add(WebViewFragment())
        return data
    }


}