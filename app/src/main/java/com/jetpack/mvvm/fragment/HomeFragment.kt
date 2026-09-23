package com.jetpack.mvvm.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.framework.mvvm.base.BaseFragment
import com.jetpack.mvvm.BR
import com.jetpack.mvvm.databinding.FragmentHomeBinding
import com.jetpack.mvvm.viewmodel.DeviceViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class HomeFragment : BaseFragment<FragmentHomeBinding>(){

    companion object{
        private const val TAG="HomeFragment"
    }

    private val viewModel by activityViewModel<DeviceViewModel>()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        this.mBinding.setVariable(BR.scaleViewModel, this.viewModel)

        initListener()
    }



    private fun initListener(){
        viewModel.onScanClickListener.observe(viewLifecycleOwner){
            Log.d("onScanClickListener","----开始扫描")
            viewModel.startScan()
        }


        viewModel.onConnectionClickListener.observe(viewLifecycleOwner){
            Log.d("onScanClickListener","----开始连接蓝牙")
            viewModel.uiState.value.device?.let {
                viewModel.connect(it)
            }

        }


        viewModel.onMeasureClickListener.observe(viewLifecycleOwner){
            Log.d("onScanClickListener","----开始测量 $it")
            viewModel.startMeasure()
        }


        lifecycleScope.launch {
            viewModel.uiState.collect {
                Toast.makeText(requireActivity(),it.error, Toast.LENGTH_SHORT).show()

            }

        }

    }

}