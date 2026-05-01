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
            val jsonString = context.assets.open("dialogues.json").bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val jsonArray = jsonObject.getJSONArray(dialogId)

            val dialogs = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                dialogs.add(jsonArray.getString(i))
            }
            dialogs
        }
    }

    suspend fun saveDialogToRoom(dialogId: String, lines: List<String>) {
        withContext(Dispatchers.IO) {
            // Сохраняем только прогресс (текущий индекс = 0)
            val progress = DialogProgress(dialogId, 0)
            AppDatabase.getInstance(context).dialogDao().save(progress)
        }
    }

    suspend fun getDialogFromRoom(dialogId: String): List<String> {
        // Диалоги не храним в Room, только прогресс. Возвращаем пустой список
        return emptyList()
    }

    suspend fun getProgress(dialogId: String): Int {
        return withContext(Dispatchers.IO) {
            val progress = AppDatabase.getInstance(context).dialogDao().get(dialogId)
            progress?.currentLine ?: 0
        }
    }

    suspend fun saveProgress(dialogId: String, currentIndex: Int, isCompleted: Boolean) {
        withContext(Dispatchers.IO) {
            AppDatabase.getInstance(context).dialogDao().save(
                DialogProgress(dialogId, currentIndex)
            )
        }
    }
}