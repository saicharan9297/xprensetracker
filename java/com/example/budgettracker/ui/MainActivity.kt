package com.example.budgettracker.ui

import android.content.pm.ActivityInfo
import com.example.budgettracker.viewmodel.AppViewModelFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.budgettracker.R
import com.example.budgettracker.repo.AppRepo
import com.example.budgettracker.viewmodel.AppViewModel
import com.example.budgettracker.db.AppDB
import com.example.budgettracker.databinding.ActivityMainBinding

/**
 * MainActivity is the entry point of the Budget Tracker app.
 * It sets up the initial fragment and handles bottom navigation
 * to switch between different fragments (HomeFragment and TransactionFragment).
 */
class MainActivity : AppCompatActivity() {
    // View binding for the activity layout
    private lateinit var binding: ActivityMainBinding

    // ViewModel for managing UI-related data
    private lateinit var appViewModel: AppViewModel

    /**
     * Called when the activity is first created. Sets up view binding, initializes
     * ViewModel, and sets the initial fragment to HomeFragment.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Lock the activity to portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)

        // Set the initial fragment
        replaceFragment(HomeFragment())

        // Set up ViewModel
        setupVM()

        // Set up the bottom navigation view item selection listener
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment()) // Navigate to HomeFragment
                R.id.transactions -> replaceFragment(TransactionFragment()) // Navigate to TransactionFragment
            }
            true
        }
    }

    /**
     * Sets up the ViewModel for this activity using AppRepo and AppViewModelFactory.
     */
    private fun setupVM() {
        // Create an instance of the repository with the database
        val appRepo = AppRepo(AppDB(this))

        // Create ViewModelProviderFactory with the repository
        val viewModelProviderFactory = AppViewModelFactory(application, appRepo)

        // Initialize the ViewModel
        appViewModel = ViewModelProvider(this, viewModelProviderFactory)[AppViewModel::class.java]
    }

    /**
     * Replaces the current fragment with the given fragment.
     * @param fragment The new fragment to display
     */
    private fun replaceFragment(fragment: Fragment) {
        // Start a fragment transaction
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Replace the existing fragment with the new fragment
        fragmentTransaction.replace(R.id.frame_layout, fragment)

        // Commit the transaction
        fragmentTransaction.commit()
    }
}
