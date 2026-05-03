package com.example.cafe.presentation.screens.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cafe.domain.usecases.LoadDialogUseCase
import com.example.cafe.domain.usecases.SaveDialogProgressUseCase
import kotlinx.coroutines.launch

class DialogViewModel(
    private val loadDialogUseCase: LoadDialogUseCase,
    private val saveProgressUseCase: SaveDialogProgressUseCase
) : ViewModel() {

    private val _dialogLines = MutableLiveData<List<String>>(emptyList())
    val dialogLines: LiveData<List<String>> = _dialogLines

    private val _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> = _currentIndex

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isComplete = MutableLiveData(false)
    val isComplete: LiveData<Boolean> = _isComplete

    private var currentDialogId = ""

    fun loadDialog(dialogId: String, reset: Boolean = false) {
        currentDialogId = dialogId
        viewModelScope.launch {
            _isLoading.value = true
            val lines = loadDialogUseCase(dialogId)
            _dialogLines.value = lines

            // Загружаем сохранённый прогресс из Room (если не сброс)
            val savedIndex = if (reset) 0 else saveProgressUseCase.getProgress(dialogId)
            _currentIndex.value = savedIndex

            _isLoading.value = false
        }
    }

    fun resetProgress(dialogId: String) {
        viewModelScope.launch {
            saveProgressUseCase.resetProgress(dialogId)
        }
    }
    fun nextLine() {
        val current = _currentIndex.value ?: 0
        val lines = _dialogLines.value ?: emptyList()

        if (current < lines.size - 1) {
            _currentIndex.value = current + 1
            saveProgress()
        } else {
            _isComplete.value = true
        }
    }

    private fun saveProgress() {
        if (currentDialogId.isNotEmpty()) {
            viewModelScope.launch {
                val index = _currentIndex.value ?: 0
                saveProgressUseCase(currentDialogId, index)
            }
        }
    }

    fun reset() {
        _currentIndex.value = 0
        _isComplete.value = false
    }
}