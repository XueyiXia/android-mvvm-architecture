package com.framework.mvvm.utils


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object PermissionManager {

    private fun checkPermission(
        context: Context,
        permission: String
    ): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }


    fun hasBluetoothScanPermission(
        context: Context
    ): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            )
        } else {
            checkPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }


    fun hasBluetoothConnectPermission(
        context: Context
    ): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            true
        }
    }


    fun hasBluetoothPermission(
        context: Context
    ): Boolean {
        return hasBluetoothScanPermission(context) &&
                hasBluetoothConnectPermission(context)
    }


    fun hasNetworkPermission(
        context: Context
    ): Boolean {
        return checkPermission(
            context,
            Manifest.permission.INTERNET
        )
    }


    fun getBluetoothPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }


    fun getAllPermissions(): Array<String> {

        val permissions = mutableListOf<String>()

        permissions.addAll(
            getBluetoothPermissions()
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        permissions.add(
            Manifest.permission.CAMERA
        )

        return permissions.toTypedArray()
    }


    fun needRequestPermission(
        context: Context
    ): Boolean {

        return getAllPermissions()
            .any { permission ->

                ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED

            }
    }


    fun getDeniedPermissions(
        context: Context
    ): Array<String> {

        return getAllPermissions()
            .filter { permission ->

                ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED

            }
            .toTypedArray()
    }
}