package com.jetpack.mvvm.activities





import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresPermission
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.framework.mvvm.base.BaseActivity
import com.jetpack.mvvm.BR
import com.jetpack.mvvm.adapter.DeviceAdapter
import com.jetpack.mvvm.bean.BleDevice
import com.jetpack.mvvm.databinding.ActivityDeviceBindBinding
import com.jetpack.mvvm.viewmodel.DeviceBindViewModel
import com.jetpack.mvvm.viewmodel.DeviceConnectState
import com.jetpack.mvvm.viewmodel.DeviceViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeviceBindActivity: BaseActivity<ActivityDeviceBindBinding>(){

    private val viewModel: DeviceBindViewModel by viewModel()

    private lateinit var deviceAdapter:DeviceAdapter


    override fun inflateBinding():ActivityDeviceBindBinding{
        return ActivityDeviceBindBinding.inflate(layoutInflater)
    }


    override fun initView(rootView:View,savedInstanceState:Bundle?){
        this.mBinding.setVariable(BR.deviceBindViewModel, this.viewModel)
        initRecycler()
        observeData()
        checkPermission()
    }


    private fun initRecycler(){

        deviceAdapter= DeviceAdapter {
            viewModel.connectDevice(it.second)
        }

        mBinding.recyclerDevice.apply{
            layoutManager =
                LinearLayoutManager(
                    this@DeviceBindActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter=this@DeviceBindActivity.deviceAdapter
        }

    }


    private fun observeData(){

        lifecycleScope.launch{

            viewModel.devices.collectLatest{
                if(it.isNotEmpty()){
                    mBinding.progressScan.visibility=
                        View.GONE


                    mBinding.viewPulse.clearAnimation()


                    mBinding.tvStatus.text=
                        "发现附近设备"

                }else{

                    mBinding.progressScan.visibility=
                        View.VISIBLE


                    mBinding.tvStatus.text=
                        "正在搜索附近设备..."

                }
                deviceAdapter.submitList(it)
                mBinding.tvEmpty.visibility=
                    if(it.isEmpty()) View.VISIBLE else View.GONE

            }

        }


        lifecycleScope.launch{

            viewModel.connectState.collectLatest{

                when(it){

                    DeviceConnectState.SCANNING->{
                        mBinding.tvStatus.text="正在搜索附近设备..."
                    }

                    DeviceConnectState.CONNECTING->{
                        mBinding.tvStatus.text="正在连接设备..."
                    }

                    DeviceConnectState.WAIT_WIFI->{
                        mBinding.tvStatus.text="正在获取设备状态..."
                    }

                    DeviceConnectState.WIFI_CONNECTED->{
//                        startActivity(
//
//                        )
                        finish()
                    }

                    DeviceConnectState.NEED_WIFI_CONFIG->{
//                        startActivity(
//                            WifiConfigActivity.newIntent(this@DeviceBindActivity)
//                        )
                        finish()
                    }

                    DeviceConnectState.ERROR->{
                        showSnackbar("设备连接失败")
                    }

                    else->{}

                }

            }

        }

    }


    private fun checkPermission(){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.S){

            requestPermissions(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                ),
                1001
            )

        }else{

            startScan()

        }

    }


    override fun onRequestPermissionsResult(
        requestCode:Int,
        permissions:Array<out String>,
        grantResults:IntArray
    ){

        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )

        if(requestCode==1001){
            startScan()
        }

    }


    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    private fun startScan(){
        startLoading()
        viewModel.startScan()
    }


    private fun startLoading(){

        mBinding.progressScan.visibility=View.VISIBLE

        val animator=
            ObjectAnimator.ofFloat(
                mBinding.viewPulse,
                "scaleX",
                1f,
                1.3f
            )

        animator.duration=1200
        animator.repeatMode=
            ValueAnimator.REVERSE

        animator.repeatCount=
            ValueAnimator.INFINITE

        animator.start()

    }


    override fun onDestroy(){
        super.onDestroy()
        viewModel.stopScan()
    }

}