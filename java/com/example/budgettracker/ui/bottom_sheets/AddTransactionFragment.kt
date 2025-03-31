package com.example.budgettracker.ui.bottom_sheets

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.budgettracker.model.Balance
import com.example.budgettracker.ui.CategorySelectionDialogFragment
import com.example.budgettracker.viewmodel.AppViewModel
import com.example.budgettracker.model.Category
import com.example.budgettracker.model.Transaction
import com.example.budgettracker.R
import com.example.budgettracker.databinding.FragmentAddTransactionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Date

/**
 * BottomSheetDialogFragment for adding a new transaction.
 * Allows the user to input transaction details including type, category, title, and amount.
 */
class AddTransactionFragment : BottomSheetDialogFragment() {

    private var addTransactionBinding: FragmentAddTransactionBinding? = null
    private val binding get() = addTransactionBinding!!

    private var transactionType: String = "Credit" // Default transaction type
    private var selectedCategory: Category? = null

    private val appViewModel: AppViewModel by activityViewModels()

    /**
     * Inflates the layout for the dialog fragment.
     *
     * @param inflater LayoutInflater to inflate the layout.
     * @param container Optional view group to contain the dialog view.
     * @param savedInstanceState Optional saved instance state for restoration.
     * @return The inflated view for the dialog.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addTransactionBinding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called after the view has been created. Sets up click listeners for buttons.
     *
     * @param view The created view for the fragment.
     * @param savedInstanceState Optional saved instance state for restoration.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set default selection for the Credit button
        binding.creditImageBtn.setImageResource(R.drawable.baseline_check_24)

        // Open category selection dialog when category input is clicked
        binding.edtAddTransCategory.setOnClickListener {
            val categories = if (transactionType == "Credit") {
                appViewModel.appRepo.getCreditCategory()
            } else {
                appViewModel.appRepo.getDebitCategory()
            }

            val dialog = CategorySelectionDialogFragment.newInstance(categories) { category ->
                selectedCategory = category
                binding.edtAddTransCategory.setText(category.name)
            }
            dialog.show(parentFragmentManager, "CategorySelectionDialog")
        }

        // Handle Credit button click
        binding.creditBtn.setOnClickListener {
            transactionType = "Credit"
            binding.creditImageBtn.setImageResource(R.drawable.baseline_check_24)
            binding.debitImageBtn.setImageResource(0)
            resetCategory() // Clear selected category when switching types
        }

        // Handle Debit button click
        binding.debitBtn.setOnClickListener {
            transactionType = "Debit"
            binding.debitImageBtn.setImageResource(R.drawable.baseline_check_24)
            binding.creditImageBtn.setImageResource(0)
            resetCategory() // Clear selected category when switching types
        }

        // Add transaction when button clicked
        binding.addTransactionBtn.setOnClickListener {
            onAddTransaction()
        }
    }

    /**
     * Validates input fields and adds a new transaction if valid.
     */
    private fun onAddTransaction() {
        val title = binding.edtAddTransTitle.text.toString().trim()
        val d = Date()
        val transactionTiming: CharSequence = DateFormat.format("yyyy MMMM d, HH:mm:ss a", d.time).toString().replace("am", "AM").replace("pm", "PM")
        val amount = binding.edtAddTransAmount.text.toString().trim()
        val category = binding.edtAddTransCategory.text.toString().trim()

        // Validate category and amount inputs
        if (category.isEmpty()) {
            Toast.makeText(requireContext(), "Please select category", Toast.LENGTH_SHORT).show()
        } else if (amount.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter amount", Toast.LENGTH_SHORT).show()
        } else {
            // Create and add transaction if inputs are valid
            val transaction = Transaction(
                0, transactionType, title, category, amount.toDouble(), transactionTiming.toString()
            )
            appViewModel.addTransaction(transaction)
            balanceUpdate(amount) // Update balance after adding transaction
        }
    }

    /**
     * Updates the balance based on the transaction type after adding a transaction.
     *
     * @param amount The amount of the transaction.
     */
    private fun balanceUpdate(amount: String) {
        val transactionAmount = amount.toDoubleOrNull() ?: 0.0
        appViewModel.getBalance().observe(viewLifecycleOwner) { balance ->
            val currentBalance = balance?.availableBalance ?: 0.0
            // Calculate updated balance based on transaction type
            val updatedBalance = if (transactionType == "Debit") {
                currentBalance - transactionAmount
            } else {
                currentBalance + transactionAmount
            }
            updateBalanceInViewModel(updatedBalance)
        }
    }

    /**
     * Updates the balance in the ViewModel and displays a success message.
     *
     * @param updatedBalance The new balance after adding the transaction.
     */
    private fun updateBalanceInViewModel(updatedBalance: Double) {
        val balance = Balance(id = 1, availableBalance = updatedBalance)
        appViewModel.updateBalance(balance)
        Toast.makeText(requireContext(), "Transaction added successfully", Toast.LENGTH_SHORT).show()
        dismiss() // Dismiss the dialog after transaction is added
    }

    /**
     * Resets the selected category to null and clears the category input field.
     */
    @SuppressLint("SetTextI18n")
    private fun resetCategory() {
        selectedCategory = null
        binding.edtAddTransCategory.setText("") // Clear the category text
    }

    /**
     * Cleanup the binding when the view is destroyed to prevent memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        addTransactionBinding = null
    }
}
