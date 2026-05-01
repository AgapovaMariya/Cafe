package com.example.cafe.domain.usecases

import com.example.cafe.data.repository.DialogRepository

class LoadDialogUseCase(
    private val repository: DialogRepository
) {
    suspend operator fun invoke(dialogId: String): List<String> {
        // Сначала пробуем загрузить из JSON
        return repository.loadDialogFromAssets(dialogId)
    }
}