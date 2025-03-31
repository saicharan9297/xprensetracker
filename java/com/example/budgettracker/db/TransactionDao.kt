package com.example.budgettracker.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgettracker.model.Transaction

/**
 * Data Access Object (DAO) for managing transactions in the database.
 *
 * This interface provides methods for performing CRUD (Create, Read, Update, Delete)
 * operations on the transactions table.
 */
@Dao
interface TransactionDao {

    /**
     * Inserts a new transaction into the database. If a transaction with the same ID exists,
     * it will be replaced.
     *
     * @param transaction The transaction object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransaction(transaction: Transaction)

    /**
     * Deletes a transaction from the database by its ID.
     *
     * @param transactionId The unique identifier of the transaction to be deleted.
     */
    @Query("DELETE FROM transactions WHERE id = :transactionId")
    suspend fun deleteTransaction(transactionId: Int)

    /**
     * Retrieves a list of transactions that occurred in a specific month.
     *
     * @param month The month to filter transactions by (in string format).
     * @return A LiveData list of transactions matching the specified month.
     */
    @Query("SELECT * FROM transactions WHERE transactionTiming LIKE '%' || :month || '%' ORDER BY id DESC")
    fun searchTransactionByMonth(month: String): LiveData<List<Transaction>>

    /**
     * Retrieves all transactions ordered by their timing in descending order.
     *
     * @return A LiveData list of all transactions.
     */
    @Query("SELECT * FROM transactions ORDER BY id DESC")
    fun getAllTransactions(): LiveData<List<Transaction>>

    /**
     * Retrieves the latest 20 transactions ordered by their timing in descending order.
     *
     * @return A LiveData list of the latest transactions.
     */
    @Query("SELECT * FROM transactions ORDER BY id DESC LIMIT 20")
    fun getLatestTransactions(): LiveData<List<Transaction>>

    /**
     * Deletes transactions that occurred in a specific month of a specific year.
     *
     * @param month The month to filter transactions to be deleted (in string format).
     * @param year The year to filter transactions to be deleted (in string format).
     */
    @Query("DELETE FROM transactions WHERE transactionTiming LIKE '%' || :month || '%' AND transactionTiming LIKE '%' || :year || '%'")
    suspend fun deleteOldTransactions(month: String, year: String)
}
