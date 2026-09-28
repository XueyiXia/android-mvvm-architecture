package com.jetpack.mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jetpack.mvvm.R
import com.jetpack.mvvm.bean.WifiInfo
import com.jetpack.mvvm.databinding.ItemWifiBinding

class WifiAdapter(
    private val click:(WifiInfo)->Unit
):
    ListAdapter<WifiInfo,WifiAdapter.VH>(
        object:DiffUtil.ItemCallback<WifiInfo>(){

            override fun areItemsTheSame(
                old:WifiInfo,
                new:WifiInfo
            ):Boolean{

                return old.ssid==new.ssid

            }


            override fun areContentsTheSame(
                old:WifiInfo,
                new:WifiInfo
            ):Boolean{

                return old==new

            }

        }
    ){


    class VH(
        val binding:ItemWifiBinding
    ):
        RecyclerView.ViewHolder(
            binding.root
        )



    override fun onCreateViewHolder(
        parent:ViewGroup,
        viewType:Int
    ):VH{

        return VH(
            ItemWifiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))

    }



    override fun onBindViewHolder(
        holder:VH,
        position:Int
    ){

        val item=getItem(position)
        holder.binding.tvSsid.text= item.ssid
        holder.binding.tvInfo.text=
            holder.binding.root.context
                .getString(
                    R.string.wifi_security,
                    item.ssid,
                    item.rssi
                )


        holder.binding.root.setOnClickListener{

            click(item)

        }

    }

}