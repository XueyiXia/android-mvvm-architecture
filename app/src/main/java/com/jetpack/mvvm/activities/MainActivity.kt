package com.jetpack.mvvm.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.framework.mvvm.base.BaseMvvmActivity
import com.framework.mvvm.viewmodel.BaseViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jetpack.mvvm.R
import com.jetpack.mvvm.databinding.ActivityMainBinding
import com.jetpack.mvvm.fragment.HomeFragment
import com.jetpack.mvvm.fragment.MallsFragment
import com.jetpack.mvvm.fragment.AddressBookFragment
import com.jetpack.mvvm.fragment.UserFragment

class MainActivity : BaseMvvmActivity<ActivityMainBinding,BaseViewModel>() {


    companion object{
        private const val TAG = "MainActivity"
        const val TAG_HOME = "TAG_HOME" //首页
        const val TAG_MALLS = "TAG_MALLS" //商城
        const val TAG_ME = "TAG_ME" //个人中心
        const val TAG_ADDRESS_BOOK = "TAG_ADDRESS_BOOK" //
    }

    private var tag: String = TAG_HOME //标识点击了那个Fragment,默认的是定位到首页

    private var mHomeFragment: HomeFragment?=null

    private var mMallsFragment: MallsFragment?=null

    private var mUserFragment: UserFragment?=null

    private var mAddressBookFragment: AddressBookFragment?=null

    private lateinit var mAHBottomNavigation: BottomNavigationView


    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        val data=intent?.extras
        Log.e("ActivityForResult", "get data--->>$data")

        mAHBottomNavigation=findViewById(R.id.bottom_navigation)


        /**
         * 切换Fragment
         */
        commitFragment(tag)


        /**
         * 实例化底部导航栏
         */
        initBottomNavigation()


        val intent=Intent()
        val bundle=Bundle()
        bundle.putInt("onDestroy",10)
        intent.putExtra("onDestroy",bundle)
        setResult(RESULT_OK,intent)
    }


    /**
     * 页面切换
     * @param tag
     */
    private fun commitFragment(tag: String) {
        this.tag = tag
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        when (tag) {
            TAG_HOME -> {
                if(mHomeFragment==null){
                    mHomeFragment=HomeFragment()
                    fragmentTransaction.add(R.id.container, mHomeFragment!!)
                }

                mHomeFragment!!.arguments = intent.extras
                fragmentTransaction.show(mHomeFragment!!)


                //商城隐藏
                if (mMallsFragment != null && mMallsFragment!!.isAdded) {
                    fragmentTransaction.hide(mMallsFragment!!)
                }

                //个人中心隐藏
                if (mUserFragment != null && mUserFragment!!.isAdded) {
                    fragmentTransaction.hide(mUserFragment!!)
                }

                if (mAddressBookFragment != null && mAddressBookFragment!!.isAdded) {
                    fragmentTransaction.hide(mAddressBookFragment!!)
                }
            }

            TAG_MALLS -> {
                if(mMallsFragment==null){
                    mMallsFragment=MallsFragment()
                    fragmentTransaction.add(R.id.container, mMallsFragment!!)
                }

                mMallsFragment!!.arguments = intent.extras
                fragmentTransaction.show(mMallsFragment!!)


                //首页隐藏
                if (mHomeFragment != null && mHomeFragment!!.isAdded) {
                    fragmentTransaction.hide(mHomeFragment!!)
                }

                //个人中心隐藏
                if (mUserFragment != null && mUserFragment!!.isAdded) {
                    fragmentTransaction.hide(mUserFragment!!)
                }

                if (mAddressBookFragment != null && mAddressBookFragment!!.isAdded) {
                    fragmentTransaction.hide(mAddressBookFragment!!)
                }
            }

            TAG_ME -> {
                if(mUserFragment==null){
                    mUserFragment=UserFragment()
                    fragmentTransaction.add(R.id.container, mUserFragment!!)
                }

                mUserFragment!!.arguments = intent.extras
                fragmentTransaction.show(mUserFragment!!)


                //首页隐藏
                if (mHomeFragment != null && mHomeFragment!!.isAdded) {
                    fragmentTransaction.hide(mHomeFragment!!)
                }

                //商城隐藏
                if (mMallsFragment != null && mMallsFragment!!.isAdded) {
                    fragmentTransaction.hide(mMallsFragment!!)
                }

                if (mAddressBookFragment != null && mAddressBookFragment!!.isAdded) {
                    fragmentTransaction.hide(mAddressBookFragment!!)
                }
            }


            TAG_ADDRESS_BOOK -> {
                if(mAddressBookFragment==null){
                    mAddressBookFragment=AddressBookFragment()
                    fragmentTransaction.add(R.id.container, mAddressBookFragment!!)
                }

                mAddressBookFragment?.arguments = intent.extras
                fragmentTransaction.show(mAddressBookFragment!!)


                //首页隐藏
                if (mHomeFragment != null && mHomeFragment?.isAdded == true) {
                    fragmentTransaction.hide(mHomeFragment!!)
                }

                //商城隐藏
                if (mMallsFragment != null && mMallsFragment?.isAdded == true) {
                    fragmentTransaction.hide(mMallsFragment!!)
                }


                //个人中心隐藏
                if (mUserFragment != null && mUserFragment!!.isAdded) {
                    fragmentTransaction.hide(mUserFragment!!)
                }
            }
        }
        fragmentTransaction.commitAllowingStateLoss()
    }


    private fun initBottomNavigation(){
        mAHBottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_search -> commitFragment(TAG_HOME)
                R.id.action_settings -> commitFragment(TAG_MALLS)
                R.id.action_navigation -> commitFragment(TAG_ME)
                R.id.action_address_book -> commitFragment(TAG_ADDRESS_BOOK)
            }

            true
        }
    }


    override fun onDestroy() {
        super.onDestroy()


    }
}