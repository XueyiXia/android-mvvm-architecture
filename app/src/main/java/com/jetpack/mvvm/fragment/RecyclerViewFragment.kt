package com.jetpack.mvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jetpack.mvvm.R
import com.jetpack.mvvm.adapter.SimpleRecyclerAdapter

class RecyclerViewFragment : Fragment() {
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recycler_view, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView?.setLayoutManager(LinearLayoutManager(activity))
        val adapter: SimpleRecyclerAdapter? = context?.let { SimpleRecyclerAdapter(it, data) }
        recyclerView?.setAdapter(adapter)

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private val THRESHOLD_LOAD_MORE = 3
            private var hasLoadMore = false

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hasLoadMore = false
                }

                if (newState != RecyclerView.SCROLL_STATE_DRAGGING && !hasLoadMore) {
                    val lastPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val offset = recyclerView.adapter!!.itemCount - lastPosition - 1
                    if (offset <= THRESHOLD_LOAD_MORE) {
                        hasLoadMore = true
                        adapter?.data=(data)
                        adapter?.notifyDataSetChanged()
                    }
                }
            }
        })

        return view
    }

    private val data: List<String>
        get() {
            val data: MutableList<String> =
                ArrayList()
            data.add("#ff9999")
            data.add("#ffaa77")
            data.add("#ff9966")
            data.add("#ffcc55")
            data.add("#ff99bb")
            data.add("#ff77dd")
            data.add("#ff33bb")
            data.add("#ff9999")
            data.add("#ffaa77")
            data.add("#ff9966")
            return data
        }
}
