package com.jetpack.mvvm.activities

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.framework.mvvm.base.BaseActivity
import com.jetpack.mvvm.BR
import com.jetpack.mvvm.R
import com.jetpack.mvvm.adapter.WifiAdapter
import com.jetpack.mvvm.bean.WifiInfo
import com.jetpack.mvvm.databinding.ActivityAddDeviceBinding
import com.jetpack.mvvm.viewmodel.AddDeviceViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddDeviceActivity : BaseActivity<ActivityAddDeviceBinding>() {


    private val viewModel by viewModel<AddDeviceViewModel>()


    private val adapter = WifiAdapter { wifi ->
        showPasswordDialog(wifi)
    }

    override fun inflateBinding(): ActivityAddDeviceBinding {
        return ActivityAddDeviceBinding.inflate(layoutInflater)
    }


    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        this.mBinding.setVariable(BR.addDeviceViewModel, this.viewModel)

        mBinding.recyclerWifi.layoutManager =
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        mBinding.recyclerWifi.adapter = adapter

        viewModel.scanWifi()

        observe()
    }

    private fun showPasswordDialog(wifi: WifiInfo) {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_wifi_password, null)
        val tvSsid = dialogView.findViewById<TextView>(R.id.tvSsid)
        val etPassword = dialogView.findViewById<EditText>(R.id.etPassword)
        val btnCancel = dialogView.findViewById<TextView>(R.id.btnCancel)
        val btnConnect = dialogView.findViewById<TextView>(R.id.btnConnect)
        tvSsid.text = wifi.ssid

        val dialog = AlertDialog.Builder(this, R.style.WifiPasswordDialog)
            .setView(dialogView)
            .create()

        btnCancel.setOnClickListener { dialog.dismiss() }

        btnConnect.setOnClickListener  {
            val password = etPassword.text?.toString().orEmpty()
            if (password.isBlank()) {
                Toast.makeText(this, R.string.wifi_password_hint, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Log.d("Wifi", "连接:${wifi.ssid},password= :${password} , password长度=${password.length}")
            viewModel.sendWifiConfig(wifi.ssid,password)
            dialog.dismiss()
        }

        dialog.show()
        applyDialogWindow(dialog)
    }

    /** 按屏宽比例设置 Dialog 宽度，配合 sw 尺寸资源做屏幕适配 */
    private fun applyDialogWindow(dialog: AlertDialog) {
        val window = dialog.window ?: return
        val metrics = resources.displayMetrics
        val width = (metrics.widthPixels * DIALOG_WIDTH_RATIO).toInt()
        window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.wifiList.collect { list ->
                    adapter.submitList(list)
                }
            }
        }
    }

    companion object {
        private const val DIALOG_WIDTH_RATIO = 0.82f
    }


    override fun onBackPressedCallback(): Boolean {
        finish()
        return false

    }
}