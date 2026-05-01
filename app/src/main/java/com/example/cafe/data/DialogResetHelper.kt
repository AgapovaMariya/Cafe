package com.example.cafe

import android.content.Context
import com.example.cafe.data.repository.DialogRepository
import kotlinx.coroutines.runBlocking

object DialogResetHelper {

    private const val PREF_NAME = "dialog_prefs"
    private const val KEY_APP_VERSION = "app_version"

    // Сброс при обновлении версии приложения
    suspend fun resetDialogsIfVersionChanged(context: Context, repository: DialogRepository) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val currentVersion = getAppVersion(context)
        val savedVersion = prefs.getInt(KEY_APP_VERSION, 0)

        if (currentVersion != savedVersion) {
            // Версия изменилась - сбрасываем диалог (добавим метод в DialogRepository)
            // Пока просто сбросим конкретный диалог
            repository.resetProgress("lady_dialog")
            prefs.edit().putInt(KEY_APP_VERSION, currentVersion).apply()
        }
    }

    // Принудительный сброс конкретного диалога
    suspend fun resetSpecificDialog(repository: DialogRepository, dialogId: String) {
        repository.resetProgress(dialogId)
    }

    private fun getAppVersion(context: Context): Int {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionCode
        } catch (e: Exception) {
            1
        }
    }
}