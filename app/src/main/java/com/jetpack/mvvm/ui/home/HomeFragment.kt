package com.jetpack.mvvm.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.framework.mvvm.base.BaseFragment
import com.jetpack.mvvm.databinding.FragmentHomeBinding
import com.jetpack.mvvm.model.Menu
import com.jetpack.mvvm.model.News
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(){

    private val homeViewModel: HomeViewModel by viewModel()

    override fun bindDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater,container,false)
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {


        mBinding.recyclerViewMenu.layoutManager = GridLayoutManager(context, 2)

        mBinding.recyclerViewNews.layoutManager = LinearLayoutManager(context)

        mBinding.recyclerViewNews.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        homeViewModel.getListMenu().observe(viewLifecycleOwner, Observer {
            val items: List<Menu> = it
            mBinding.recyclerViewMenu.adapter = MenuAdapter(items, requireContext())
        })

        homeViewModel.getListNews().observe(viewLifecycleOwner, Observer {
            val items: List<News> = it
            mBinding.recyclerViewNews?.adapter = NewsAdapter(items,requireContext())
        })
    }




}
