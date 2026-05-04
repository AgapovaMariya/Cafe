// CafeApplication.kt
package com.example.cafe

import android.app.Application
import com.example.cafe.data.repository.DialogRepository
import com.example.cafe.domain.usecases.LoadDialogUseCase
import com.example.cafe.domain.usecases.SaveDialogProgressUseCase
import com.example.cafe.presentation.screens.dialog.DialogViewModelFactory

class CafeApplication : Application() {

    lateinit var loadDialogUseCase: LoadDialogUseCase
        private set
    lateinit var saveDialogProgressUseCase: SaveDialogProgressUseCase
        private set
    lateinit var dialogViewModelFactory: DialogViewModelFactory   // <- это поле должно быть
        private set

    override fun onCreate() {
        super.onCreate()

        // Репозиторий
        val dialogRepository = DialogRepository(applicationContext)

        // UseCase
        loadDialogUseCase = LoadDialogUseCase(dialogRepository)
        saveDialogProgressUseCase = SaveDialogProgressUseCase(dialogRepository)

        // Фабрика ViewModel
        dialogViewModelFactory = DialogViewModelFactory(loadDialogUseCase, saveDialogProgressUseCase)
    }
}