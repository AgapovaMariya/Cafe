package com.example.cafe.data.repository

import android.content.Context
import com.example.cafe.data.database.AppDatabase
import com.example.cafe.data.database.DialogProgress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class DialogRepository(private val context: Context) {

    suspend fun loadDialogFromAssets(dialogId: String): List<String> {
        return withContext(Dispatchers.IO) {
            val jsonString = context.assets.open("dialogues.json")
                .bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val jsonArray = jsonObject.getJSONArray(dialogId)

            val dialogs = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                dialogs.add(jsonArray.getString(i))
            }
            dialogs
        }
    }

    suspend fun getProgress(dialogId: String): Int {
        return withContext(Dispatchers.IO) {
            val progress = AppDatabase.getInstance(context).dialogDao().get(dialogId)
            progress?.currentLine ?: 0
        }
    }

    suspend fun saveProgress(dialogId: String, currentIndex: Int) {
        withContext(Dispatchers.IO) {
            AppDatabase.getInstance(context).dialogDao().save(
                DialogProgress(dialogId, currentIndex)
            )
        }
    }

    suspend fun resetProgress(dialogId: String) {
        withContext(Dispatchers.IO) {
            AppDatabase.getInstance(context).dialogDao().deleteProgress(dialogId)
        }
    }
}