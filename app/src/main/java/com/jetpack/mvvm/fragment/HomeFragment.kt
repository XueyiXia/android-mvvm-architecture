package com.jetpack.mvvm.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.framework.mvvm.base.BaseFragment
import com.jetpack.mvvm.BR
import com.jetpack.mvvm.ble.DeviceState
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

        observeState()
    }



    private fun initListener(){
        viewModel.onScanClickListener.observe(viewLifecycleOwner){
            Log.d("onScanClickListener","----开始扫描")
            viewModel.startScan()
        }


        viewModel.onConnectionClickListener.observe(viewLifecycleOwner){
            Log.d("onScanClickListener","----开始连接蓝牙")
//            viewModel.uiState.value.device?.let {
//                viewModel.connect(it)
//            }

        }


        viewModel.onMeasureClickListener.observe(viewLifecycleOwner){
            Log.d("onScanClickListener","----开始测量 $it")
            viewModel.startMeasure()
        }


        lifecycleScope.launch {
            viewModel.uiState.collect {


            }

        }

    }


    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    renderState(it.deviceState)
                }
            }
        }
    }

    private fun renderState(state: DeviceState) {
        mBinding.layoutConnect.root.visibility = View.GONE
        mBinding.layoutMeasure.root.visibility = View.GONE
        mBinding.layoutResult.root.visibility = View.GONE
        Log.d("renderState","----state $state")
        when(state) {
            DeviceState.DISCONNECTED,
            DeviceState.SCANNING,
            DeviceState.CONNECTING,
            DeviceState.READY,
            DeviceState.ERROR -> {
                mBinding.layoutConnect.root.visibility = View.VISIBLE
            }
            DeviceState.MEASURING -> {
                mBinding.layoutMeasure.root.visibility = View.VISIBLE
            }
            DeviceState.COMPLETE -> {
                mBinding.layoutResult.root.visibility = View.VISIBLE
            }
        }
    }

}