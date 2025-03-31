package com.example.budgettracker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Data class representing the balance of the user's budget.
 *
 * @property id The unique identifier for the balance entry. Default is 1.
 * @property availableBalance The available balance amount, represented as a Double.
 */
@Entity(tableName = "balance")
@Parcelize
data class Balance(
    @PrimaryKey
    val id: Int = 1, // Unique ID for the balance, defaulting to 1
    val availableBalance: Double // Current available balance amount
) : Parcelable // This class can be serialized and passed between components
