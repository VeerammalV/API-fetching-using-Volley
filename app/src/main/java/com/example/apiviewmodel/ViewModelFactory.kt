package com.example.apiviewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//ViewModelFactory is responsible for creating ViewModel instances. It's using the ViewModelProvider.NewInstanceFactory() as a base class.

// Custom ViewModelFactory class that extends ViewModelProvider.NewInstanceFactory
class ViewModelFactory (private val application: Application): ViewModelProvider.NewInstanceFactory() {

    // Override the create method to provide ViewModel instances
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            // Check if the requested ViewModel is of type ViewModel
            when {
                isAssignableFrom(com.example.apiviewmodel.ViewModel::class.java) ->
                    // If it is, return an instance of ViewModel using getInstance method
                    com.example.apiviewmodel.ViewModel.getInstance(application) as T
                else -> throw IllegalAccessException("Unknown viewmodel class $modelClass")
            }
        }

    companion object {
        // Volatile variable to ensure visibility of changes across threads
        @Volatile
        private var instance : ViewModelFactory? = null

        // Method to get an instance of ViewModelFactory
        fun getInstance(application: Application): ViewModelFactory =
            instance?: synchronized(this) {
                instance?: ViewModelFactory(application).also {
                    instance = it
                }
            }
        }
    }