package com.example.cafe.domain.usecases

import com.example.cafe.data.repository.DialogRepository

class SaveDialogProgressUseCase(
    private val repository: DialogRepository
) {
    suspend operator fun invoke(dialogId: String, currentIndex: Int) {
        repository.saveProgress(dialogId, currentIndex)
    }
}