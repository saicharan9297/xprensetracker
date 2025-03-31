package com.example.budgettracker.util

import java.text.DecimalFormat

/**
 * CurrencyFormatter is a utility object that provides functions
 * for formatting amounts in the Indian currency format.
 */
object CurrencyFormatter {

    /**
     * Formats the given amount to the Indian currency format with commas.
     *
     * @param amount The amount to be formatted as a Double.
     * @return The formatted amount as a String, without ".00" if it's a whole number.
     */
    fun formatIndianCurrency(amount: Double): String {
        // Define the Indian currency format pattern
        val format = DecimalFormat("#,##,##0.00")

        // Format the amount using the specified pattern
        val formattedAmount = format.format(amount)

        // Check if the formatted amount ends with ".00" (indicating no decimal part)
        // and remove it if present
        return if (formattedAmount.endsWith(".00")) {
            formattedAmount.substring(0, formattedAmount.length - 3)
        } else {
            formattedAmount
        }
    }
}
