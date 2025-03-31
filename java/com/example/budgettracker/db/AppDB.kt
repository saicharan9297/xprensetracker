package com.example.budgettracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.budgettracker.model.Balance
import com.example.budgettracker.model.Transaction

/**
 * The Room Database class for the Budget Tracker application.
 *
 * This class serves as the main database for storing balance and transaction records.
 * It defines the entities (tables) managed by the database and provides DAOs for data access.
 */
@Database(entities = [Balance::class, Transaction::class], version = 1)
abstract class AppDB : RoomDatabase() {

    /**
     * Provides access to the BalanceDao for performing CRUD operations on the balance table.
     *
     * @return BalanceDao instance.
     */
    abstract fun getBalanceDao(): BalanceDao

    /**
     * Provides access to the TransactionDao for performing CRUD operations on the transactions table.
     *
     * @return TransactionDao instance.
     */
    abstract fun getTransactionDao(): TransactionDao

    companion object {
        @Volatile
        private var instance: AppDB? = null
        private val LOCK = Any()  // Lock for thread-safe singleton access

        /**
         * Provides a singleton instance of the AppDB database.
         *
         * This method ensures that only one instance of the database is created and accessed.
         * It synchronizes access to the database to prevent concurrent initialization.
         *
         * @param context The application context.
         * @return An instance of the AppDB database.
         */
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        /**
         * Creates the Room database instance.
         *
         * @param context The application context.
         * @return A newly created instance of AppDB.
         */
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDB::class.java,
                name = "budget_tracker_db"  // Name of the database file
            ).build()
    }
}
