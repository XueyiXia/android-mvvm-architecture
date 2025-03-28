package com.jetpack.mvvm.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.framework.mvvm.livedata.ListLiveData
import com.jetpack.mvvm.R
import com.jetpack.mvvm.model.Menu
import com.jetpack.mvvm.model.News

class HomeViewModel : ViewModel() {

    private val listMenu = ListLiveData<Menu>()
    private val listNews = ListLiveData<News>()

    fun getListMenu(): ListLiveData<Menu> {
        listMenu.value =  listOf(
            Menu(id = 1, name = R.string.menu_item_1, color = R.color.lightTeal),
            Menu(id = 1, name = R.string.menu_item_2, color = R.color.lightRed),
            Menu(id = 1, name = R.string.menu_item_3, color = R.color.lightBlue),
            Menu(id = 1, name = R.string.menu_item_4, color = R.color.lightYellow),
            Menu(id = 1, name = R.string.menu_item_5, color = R.color.lightPurple),
            Menu(id = 1, name = R.string.menu_item_6, color = R.color.lightBrown)
        )
        return listMenu
    }

    fun getListNews(): LiveData<List<News>> {
        listNews.value =  listOf(
            News(),
            News(),
            News(),
            News(),
            News(),
            News(),
            News(),
            News()
        )
        return listNews
    }
}
