package com.jetpack.mvvm.repo

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jetpack.mvvm.repo.data.HoldingRow
import com.jetpack.mvvm.repo.data.PropertiesRow
import com.jetpack.mvvm.repo.data.QuoteRow


@Database(
    entities = [QuoteRow::class, HoldingRow::class, PropertiesRow::class],
    version = 6,
    exportSchema = true
)
abstract class QuotesDB : RoomDatabase() {
  abstract fun quoteDao(): QuoteDao
}