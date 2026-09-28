package com.jetpack.mvvm

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage

class MqttManager(private val context: Context){

    private val server="tcp://192.168.0.100:2883"
    private val dataTopic="scale/scale001/data"
    private val commandTopic="scale/scale001/command"

    private lateinit var client: MqttAndroidClient

    private val _data=MutableSharedFlow<String>()
    val data=_data

    fun connect(){
        client=MqttAndroidClient(
            context,
            server,
            "android_${System.currentTimeMillis()}"
        )

        client.setCallback(object: MqttCallback {

            override fun connectionLost(cause:Throwable?){
                Log.e("MQTT","lost")
            }

            override fun messageArrived(
                topic:String?,
                message: MqttMessage?
            ){
                val json=message?.payload?.toString(Charsets.UTF_8)?:return
                Log.d("MQTT",json)
                _data.tryEmit(json)
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?){}

        })

        val options= MqttConnectOptions().apply{
            isAutomaticReconnect=true
            isCleanSession=false
        }

        client.connect(options,null,object: IMqttActionListener {

            override fun onSuccess(token: IMqttToken?){
                Log.d("MQTT","connected")
                client.subscribe(dataTopic,1)
            }

            override fun onFailure(
                token:IMqttToken?,
                e:Throwable?
            ){
                Log.e("MQTT","connect fail")
            }

        })

    }

    fun startMeasure(){
        val json="""{"cmd":"START_MEASURE"}"""
        client.publish(
            commandTopic,
            MqttMessage(json.toByteArray())
        )

    }

    fun close(){
        if(::client.isInitialized&&client.isConnected){
            client.disconnect()
        }

    }

}