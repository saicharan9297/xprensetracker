package com.example.budgettracker.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a category of transactions in the budget tracker.
 *
 * @property id The unique identifier for the category.
 * @property name The name of the category (e.g., "Food", "Utilities").
 * @property icon The resource ID of the icon representing the category.
 */
@Parcelize
data class Category(
    val id: Int, // Unique ID for the category
    val name: String, // Name of the category
    val icon: Int // Resource ID for the category icon
) : Parcelable // This class can be serialized and passed between components
