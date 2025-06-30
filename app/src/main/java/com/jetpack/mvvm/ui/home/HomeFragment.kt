package com.jetpack.mvvm.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.framework.mvvm.base.BaseFragment
import com.jetpack.mvvm.databinding.FragmentHomeBinding
import com.jetpack.mvvm.ui.home.HomeViewModel.SettingsUiState.Loading
import com.jetpack.mvvm.ui.home.HomeViewModel.SettingsUiState.Success
import com.jetpack.mvvm.ui.watchlist.StocksAdapter
import com.jetpack.mvvm.viewmodel.CommonViewModel.SettingsUiState.Success
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeFragment : BaseFragment<FragmentHomeBinding>(){

    private val viewModel: HomeViewModel by viewModel()

    private val stocksAdapter by lazy { StocksAdapter( ) }


    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()


    override fun bindDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater,container,false)
    }



    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        initAdapter()

        callApi()


        when (settingsUiState) {
            Loading -> { //失败 do some things

            }

            is Success -> { //成功 do some things
               TODO()
            }
        }
    }



    private fun initAdapter(){
        mBinding.recyclerViewNews.layoutManager = LinearLayoutManager(context)
        mBinding.recyclerViewNews.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        mBinding.recyclerViewNews.adapter = stocksAdapter
    }

    private fun callApi(){
        viewModel.portfolio.observe(viewLifecycleOwner) {
            Timber.tag("callApi").i("$it")
            stocksAdapter.refresh()
        }
        viewModel.fetchPortfolioInRealTime()
    }

}
