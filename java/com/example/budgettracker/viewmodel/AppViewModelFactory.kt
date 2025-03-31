package com.example.budgettracker.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.budgettracker.repo.AppRepo

/**
 * Factory class for creating an instance of AppViewModel with required dependencies.
 *
 * @param application The Application context.
 * @param appRepo The repository instance used by the ViewModel.
 */
class AppViewModelFactory(
    private val application: Application,
    private val appRepo: AppRepo
) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of the given ViewModel class.
     *
     * @param modelClass The class of the ViewModel to create an instance of.
     * @return A newly created ViewModel instance.
     * @throws IllegalArgumentException if the ViewModel class is unknown.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the modelClass is assignable from AppViewModel
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            // Return an instance of AppViewModel with the required dependencies
            return AppViewModel(application, appRepo) as T
        }
        // Throw an exception if the ViewModel class is unknown
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
