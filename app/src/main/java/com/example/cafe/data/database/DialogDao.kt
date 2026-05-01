package com.example.cafe.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DialogDao {
    @Query("SELECT * FROM dialog_progress WHERE dialogId = :dialogId")
    fun get(dialogId: String): DialogProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(progress: DialogProgress)
}