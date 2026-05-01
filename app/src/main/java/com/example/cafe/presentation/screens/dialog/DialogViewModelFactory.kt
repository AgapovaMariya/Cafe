package com.example.cafe.presentation.screens.dialog


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cafe.domain.usecases.LoadDialogUseCase
import com.example.cafe.domain.usecases.SaveDialogProgressUseCase

class DialogViewModelFactory(
    private val loadDialogUseCase: LoadDialogUseCase,
    private val saveProgressUseCase: SaveDialogProgressUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DialogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DialogViewModel(loadDialogUseCase, saveProgressUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}