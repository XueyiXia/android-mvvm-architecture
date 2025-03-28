package com.framework.http.bean

import android.app.NotificationManager

/**
 * @author: xiaxueyi
 * @date: 2023-03-31
 * @time: 15:28
 * @说明:
 */

class NotificationInfo constructor(groupId: String,
                                   groupName: String,
                                   channelId: String,
                                   channelName: String){

    private var groupId: String = groupId
    private var groupName: String = groupName
    private var channelId: String = channelId
    private var channelName: String = channelName
    private var importance = NotificationManager.IMPORTANCE_DEFAULT


    fun getGroupId(): String {
        return groupId
    }

    fun setGroupId(groupId: String) {
        this.groupId = groupId
    }

    fun getGroupName(): String {
        return groupName
    }

    fun setGroupName(groupName: String) {
        this.groupName = groupName
    }

    fun getChannelId(): String {
        return channelId
    }

    fun setChannelId(channelId: String) {
        this.channelId = channelId
    }

    fun getChannelName(): String {
        return channelName
    }

    fun setChannelName(channelName: String) {
        this.channelName = channelName
    }

    fun getImportance(): Int {
        return importance
    }

    fun setImportance(importance: Int) {
        this.importance = importance
    }
}