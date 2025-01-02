package com.jetpack.mvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.jetpack.mvvm.R

class WebViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_web_view, container, false)
        val webView=view.findViewById<WebView>(R.id.webView)
        webView.loadUrl("https://github.com/XueyiXia/android-mvvm-architecture")
//        view.findViewById<WebView>(R.id.webView).setOnClickListener {
//            Toast.makeText(
//                activity,
//                "icon clicked",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
        return view
    }
}
