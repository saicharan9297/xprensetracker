package com.example.budgettracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.model.Balance
import com.example.budgettracker.repo.AppRepo
import com.example.budgettracker.model.Transaction
import kotlinx.coroutines.launch

/**
 * AppViewModel is responsible for providing data to the UI and handling user interactions
 * in a lifecycle-conscious way. It manages data from the repository and performs background
 * operations using Coroutines.
 *
 * @param app The application context.
 * @param appRepo The repository instance to access data and perform operations.
 */
class AppViewModel(app: Application, val appRepo: AppRepo) : AndroidViewModel(app)  {

    init {
        // Launch a coroutine to initialize balance and delete old transactions when the ViewModel is created
        viewModelScope.launch {
            appRepo.addInitialBalanceIfNotExists() // Initialize balance when the app starts
            appRepo.deleteTransactionsOlderThanOneYear() // Delete transactions older than one year
        }
    }

    /**
     * Updates the balance in the repository.
     * @param balance The balance object to be updated.
     */
    fun updateBalance(balance: Balance) =
        viewModelScope.launch {
            appRepo.updateBalance(balance)
        }

    /**
     * Adds a new transaction to the repository.
     * @param transaction The transaction object to be added.
     */
    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            appRepo.addTransaction(transaction)
        }
    }

    /**
     * Retrieves all transactions from the repository.
     * @return A LiveData list of all transactions.
     */
    fun getAllTransaction() = appRepo.getAllTransaction()

    /**
     * Retrieves the latest transactions from the repository.
     * @return A LiveData list of the latest transactions.
     */
    fun getLatestTransactions() = appRepo.getLatestTransactions()

    /**
     * Retrieves the current balance from the repository.
     * @return A LiveData object containing the current balance.
     */
    fun getBalance() = appRepo.getBalance()

    /**
     * Searches for transactions based on the given month.
     * @param month The month to filter transactions by.
     * @return A LiveData list of transactions for the specified month.
     */
    fun searchTransactionByMonth(month: String) = appRepo.searchTransactionByMonth(month)

    /**
     * Deletes a specified transaction from the repository.
     * @param transaction The transaction object to be deleted.
     */
    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            appRepo.deleteTransaction(transaction)
        }
    }
}
