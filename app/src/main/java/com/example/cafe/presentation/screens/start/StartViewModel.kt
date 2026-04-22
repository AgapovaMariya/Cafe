package com.example.cafe.presentation.screens.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.app.Application

class StartViewModel(
    private val app: Application
) : ViewModel() {

    private val _uiState = MutableLiveData<StartUiState>(StartUiState.Idle)
    val uiState: LiveData<StartUiState> = _uiState

    fun onStartClick() {
        // Пока ничего не делает
    }

    fun onExitClick() {
        android.os.Process.killProcess(android.os.Process.myPid())
    }
}