package com.framework.mvvm.base

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.framework.mvvm.viewmodel.BaseViewModel

/**
 * @author: xiaxueyi
 * @date: 2022-11-29
 * @time: 10:20
 * @说明:
 */

abstract class BaseActivity <VM: BaseViewModel,VB: ViewDataBinding,repository:BaseRepository> : AppCompatActivity(){


    private lateinit var mViewModel:VM;

    private lateinit var mViewDataBinding:VB;

    private lateinit var mRepository:repository;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutResId())

    }

}