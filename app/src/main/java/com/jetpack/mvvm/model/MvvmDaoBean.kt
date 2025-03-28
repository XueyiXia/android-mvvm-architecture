package com.jetpack.mvvm.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MvvmDaoBean(
    @PrimaryKey
    @NonNull
    var id: String = ""
)