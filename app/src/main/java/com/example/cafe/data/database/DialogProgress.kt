package com.example.cafe.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dialog_progress")
data class DialogProgress(
    @PrimaryKey
    val dialogId: String,
    val currentLine: Int
)