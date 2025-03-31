package com.example.budgettracker.repo

import android.text.format.DateFormat
import com.example.budgettracker.model.Balance
import com.example.budgettracker.model.Category
import com.example.budgettracker.model.Transaction
import com.example.budgettracker.db.AppDB
import com.example.budgettracker.R
import java.util.Date

/**
 * Repository class that handles data operations related to transactions and balances.
 * It provides methods to add, update, delete, and retrieve transactions and balances.
 *
 * @property db The instance of the AppDB database for accessing DAOs.
 */
class AppRepo(private val db: AppDB) {

    /**
     * Adds an initial balance if it does not already exist in the database.
     */
    suspend fun addInitialBalanceIfNotExists() {
        val exists = db.getBalanceDao().checkIfExists()
        if (exists == 0) {
            val initialBalance = Balance(id = 1, availableBalance = 0.0)
            db.getBalanceDao().addBalance(initialBalance)
        }
    }

    /**
     * Retrieves a list of debit categories.
     *
     * @return A list of Category objects representing debit categories.
     */
    fun getDebitCategory(): List<Category> {
        return listOf(
            Category(1, "Salary", R.drawable.wages),
            Category(2, "Gift & Awards", R.drawable.award),
            // Essentials
            Category(3, "Rent", R.drawable.rent),
            Category(4, "Grocery", R.drawable.grocery),
            Category(5, "Milk", R.drawable.milk),
            Category(6, "Water", R.drawable.drop),
            Category(7, "Electricity", R.drawable.electricity),
            Category(8, "Phone Expenses", R.drawable.bill),
            Category(9, "Internet", R.drawable.baseline_wifi_24),
            Category(10, "Fuel", R.drawable.fuel),
            Category(11, "Gas", R.drawable.gas_cylinder),
            // Bills & Loans
            Category(12, "EMI", R.drawable.money),
            Category(13, "Insurance", R.drawable.insurance),
            Category(14, "Card Bill", R.drawable.card_bill),
            // Food & Leisure
            Category(15, "Food & Dining", R.drawable.fastfood),
            Category(16, "Entertainment", R.drawable.entertainment),
            Category(17, "Travel", R.drawable.travel),
            Category(18, "Clothing", R.drawable.clothes),
            // Health & Personal Care
            Category(19, "Healthcare", R.drawable.healthcare),
            Category(20, "Personal Care", R.drawable.personal_care),
            // Education & Investments
            Category(21, "Education", R.drawable.education),
            Category(22, "Investments", R.drawable.investment),
            // Miscellaneous
            Category(23, "Transportation", R.drawable.transport),
            Category(24, "Electronics", R.drawable.electronics),
            Category(25, "Subscriptions", R.drawable.subscription_fee),
            Category(26, "Donations", R.drawable.donate),
            Category(27, "Miscellaneous", R.drawable.miscellaneous) // Fixed duplicate ID
        )
    }

    /**
     * Retrieves a list of credit categories.
     *
     * @return A list of Category objects representing credit categories.
     */
    fun getCreditCategory(): List<Category> {
        return listOf(
            Category(1, "Salary", R.drawable.wages),
            Category(2, "Rent", R.drawable.rent),
            Category(3, "Cashback", R.drawable.cashback),
            Category(4, "Loan", R.drawable.loan),
            Category(5, "Investment Withdraw", R.drawable.withdraw),
            Category(6, "Gift & Award", R.drawable.award),
        )
    }

    /**
     * Adds a transaction to the database.
     *
     * @param transaction The Transaction object to be added.
     */
    suspend fun addTransaction(transaction: Transaction) = db.getTransactionDao().addTransaction(transaction)

    /**
     * Updates the available balance in the database.
     *
     * @param balance The Balance object containing the updated balance.
     */
    suspend fun updateBalance(balance: Balance) = db.getBalanceDao().updateBalance(balance)

    /**
     * Deletes a transaction from the database.
     *
     * @param transaction The Transaction object to be deleted.
     */
    suspend fun deleteTransaction(transaction: Transaction) = db.getTransactionDao().deleteTransaction(transaction.id)

    /**
     * Retrieves the current balance from the database.
     *
     * @return A LiveData object containing the current Balance.
     */
    fun getBalance() = db.getBalanceDao().getBalance()

    /**
     * Retrieves all transactions from the database.
     *
     * @return A LiveData object containing a list of all Transaction objects.
     */
    fun getAllTransaction() = db.getTransactionDao().getAllTransactions()

    /**
     * Retrieves the latest transactions from the database.
     *
     * @return A LiveData object containing a list of the latest Transaction objects.
     */
    fun getLatestTransactions() = db.getTransactionDao().getLatestTransactions()

    /**
     * Searches for transactions by month.
     *
     * @param month The month to search for transactions.
     * @return A LiveData object containing a list of Transaction objects for the specified month.
     */
    fun searchTransactionByMonth(month: String) = db.getTransactionDao().searchTransactionByMonth(month)

    /**
     * Deletes transactions older than one year from the database.
     */
    suspend fun deleteTransactionsOlderThanOneYear() {
        val currentYear = DateFormat.format("yyyy", Date()).toString().toInt()
        val previousYear = (currentYear - 1).toString()
        val months = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        // Delete transactions for each month of the previous year
        for (month in months) {
            db.getTransactionDao().deleteOldTransactions(month, previousYear)
        }
    }
}
