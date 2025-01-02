package com.jetpack.mvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.jetpack.mvvm.R

class LinearFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_linear, container, false)
        view.findViewById<View>(R.id.image).setOnClickListener {
            Toast.makeText(
                activity,
                "icon clicked",
                Toast.LENGTH_SHORT
            ).show()
        }
        return view
    }
}
