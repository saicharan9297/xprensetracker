package com.example.budgettracker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a financial transaction in the budget tracker.
 *
 * @property id The unique identifier for the transaction, automatically generated.
 * @property type The type of the transaction (e.g., "Credit" or "Debit").
 * @property title A brief description or title of the transaction.
 * @property category The category to which the transaction belongs (e.g., "Groceries", "Rent").
 * @property amount The monetary amount of the transaction.
 * @property transactionTiming The timestamp of when the transaction occurred, formatted as a string.
 */
@Entity(tableName = "transactions")
@Parcelize
data class Transaction(
    @PrimaryKey(autoGenerate = true) // Automatically generates a unique ID for each transaction
    var id: Int, // Unique ID for the transaction
    var type: String, // Type of transaction ("Credit" or "Debit")
    var title: String, // Title or description of the transaction
    var category: String, // Category of the transaction (e.g., "Food", "Utilities")
    var amount: Double, // Amount of money for the transaction
    var transactionTiming: String // Date and time when the transaction occurred
) : Parcelable // This class can be serialized and passed between components
