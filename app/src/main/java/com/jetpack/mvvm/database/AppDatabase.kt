package com.jetpack.mvvm.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jetpack.mvvm.database.dao.MvvmDataDAO
import com.jetpack.mvvm.model.MvvmDaoBean

@Database(entities = [MvvmDaoBean::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pokemonDAO(): MvvmDataDAO
}
