package com.example.cafe.presentation.screens.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import android.app.Application

class StartViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StartViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}