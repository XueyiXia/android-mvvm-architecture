package com.jetpack.mvvm.adapter


import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jetpack.mvvm.bean.BleDevice
import com.jetpack.mvvm.databinding.ItemBleDeviceBinding

class DeviceAdapter(
    private val onClick:(Pair<BleDevice, BluetoothDevice>)->Unit
):ListAdapter<Pair<BleDevice,BluetoothDevice>,DeviceAdapter.ViewHolder>(DiffCallback()){


    override fun onCreateViewHolder(
        parent:ViewGroup,
        viewType:Int
    ):ViewHolder{

        return ViewHolder(
            ItemBleDeviceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }


    override fun onBindViewHolder(
        holder:ViewHolder,
        position:Int
    ){

        holder.bind(
            getItem(position)
        )

    }


    inner class ViewHolder(
        private val binding:ItemBleDeviceBinding
    ):RecyclerView.ViewHolder(binding.root){


        fun bind(item:Pair<BleDevice,BluetoothDevice>){

            binding.tvDeviceName.text=
                if(item.first.name.isNullOrEmpty())
                    "ESP32_HEALTH"
                else
                    item.first.name


            binding.tvDeviceMac.text=item.first.address


            binding.root.setOnClickListener{

                onClick(item)

            }

        }

    }


    class DiffCallback:DiffUtil.ItemCallback<Pair<BleDevice,BluetoothDevice>>(){

        override fun areItemsTheSame(
            oldItem:Pair<BleDevice,BluetoothDevice>,
            newItem:Pair<BleDevice,BluetoothDevice>
        ):Boolean{

            return oldItem.first.address==newItem.first.address

        }


        override fun areContentsTheSame(
            oldItem:Pair<BleDevice,BluetoothDevice>,
            newItem:Pair<BleDevice,BluetoothDevice>
        ):Boolean{

            return oldItem==newItem

        }

    }

}