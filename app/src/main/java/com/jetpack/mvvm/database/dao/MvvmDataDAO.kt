package com.jetpack.mvvm.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jetpack.mvvm.model.MvvmDaoBean

@Dao
interface MvvmDataDAO {

    @Query("SELECT * FROM MvvmDaoBean WHERE id = :id")
    fun getById(id: String?): LiveData<MvvmDaoBean>

    @Query("SELECT * FROM MvvmDaoBean WHERE id IN(:evolutionIds)")
    fun getEvolutionsByIds(evolutionIds: List<String>): LiveData<List<MvvmDaoBean>>

    @Query("SELECT * FROM MvvmDaoBean")
    fun all(): LiveData<List<MvvmDaoBean>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(pokemon: List<MvvmDaoBean>)

    @Query("DELETE FROM MvvmDaoBean")
    fun deleteAll()

    @Delete
    fun delete(model: MvvmDaoBean)
}
