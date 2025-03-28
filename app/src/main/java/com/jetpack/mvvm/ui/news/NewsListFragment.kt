package com.jetpack.mvvm.ui.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.framework.http.http.RxHttp
import com.framework.http.interfac.SimpleResponseListener
import com.framework.mvvm.base.BaseFragment
import com.jetpack.mvvm.adapter.news.NewsAdapter
import com.jetpack.mvvm.bean.news.NewsListBean
import com.jetpack.mvvm.databinding.FragmentNewsListBinding
import com.jetpack.mvvm.ui.news.viewmodel.NewsViewModel
import com.rxjava_retrofit.HttpApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.TreeMap

class NewsListFragment : BaseFragment<FragmentNewsListBinding>() {

    companion object{
        private const val TAG="NewsListFragment";
    }

    private val viewModel by viewModel<NewsViewModel>()

    private var mNewsAdapter: NewsAdapter? = null

    private var dataList: MutableList<NewsListBean.Issue.Item>? = mutableListOf()

    private var parameter = TreeMap<String,Any>().apply {
        this["num"] = "1"
    }


    private val linearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }


    override fun bindDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): FragmentNewsListBinding {
        return FragmentNewsListBinding.inflate(inflater,container,attachToParent)
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        initAdapter()


        viewModel.refreshData(requireActivity())

//        initRequestHttp()
    }



    private fun initAdapter(){
        mNewsAdapter=  NewsAdapter(requireContext(), dataList )
        mBinding.recyclerView.adapter = mNewsAdapter
        mBinding.recyclerView.layoutManager = linearLayoutManager
        mBinding.recyclerView.itemAnimator = DefaultItemAnimator()
    }




    private fun initRequestHttp(){
        RxHttp.getInstance()
            .setApiUrl(HttpApi.test)
            .setLifecycle(this)
            .setParameter(parameter)
            .setContext(requireContext())
            .get()
            .build()
            .execute(object : SimpleResponseListener<NewsListBean>() {
                override fun onSucceed(data: NewsListBean, method: String) {
                    super.onSucceed(data, method)
                    Log.e(TAG,"输出的数据(onSuccess)${data}")
                    data?.let {
                        dataList?.addAll( it.issueList[0].itemList.filterNotNull())
                        mNewsAdapter?.notifyItemRangeChanged(0,it.issueList[0].itemList.size)
                    }

//                    if(data.getData() is HomeBean ){
//                        val bean=data.getData()
//                        bean?.let {
//                             Log.e(TAG,"输出的数据(isSuccess)${data.isSuccess()}")
//                            dataList.addAll( it.issueList[0].itemList)
//                            mHomeAdapter?.notifyItemRangeChanged(0,it.issueList[0].itemList.size)
//                        }
//                    }
                }

                override fun onCompleted() {
                    super.onCompleted()
                    Log.e(TAG,"输出的数据(onCompleted)")
                }

                override fun onError(e: Throwable?) {
                    super.onError(e)
                    Log.e(TAG,"(onError)${e}")
                }
            })
    }
}
