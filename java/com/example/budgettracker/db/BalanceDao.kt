package com.example.budgettracker.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.budgettracker.model.Balance

/**
 * Data Access Object (DAO) for managing balance records in the database.
 *
 * This interface provides methods for performing CRUD (Create, Read, Update, Delete)
 * operations on the balance table.
 */
@Dao
interface BalanceDao {

    /**
     * Inserts a new balance record into the database. If a balance record with the same ID exists,
     * it will be replaced.
     *
     * @param balance The balance object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBalance(balance: Balance)

    /**
     * Updates an existing balance record in the database.
     *
     * @param balance The balance object containing updated information.
     */
    @Update
    suspend fun updateBalance(balance: Balance)

    /**
     * Checks if a balance record with the ID of 1 exists in the database.
     *
     * @return The count of balance records with ID 1. Returns 0 if it does not exist,
     *         otherwise returns 1.
     */
    @Query("SELECT COUNT(*) FROM balance WHERE id = 1")
    suspend fun checkIfExists(): Int

    /**
     * Retrieves the balance record with the ID of 1 from the database.
     *
     * @return A LiveData object containing the balance record.
     */
    @Query("SELECT * FROM balance WHERE id = 1")
    fun getBalance(): LiveData<Balance>
}
