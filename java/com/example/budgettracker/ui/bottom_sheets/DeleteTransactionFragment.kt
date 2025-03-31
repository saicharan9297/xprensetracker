package com.example.budgettracker.ui.bottom_sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.budgettracker.model.Balance
import com.example.budgettracker.viewmodel.AppViewModel
import com.example.budgettracker.model.Transaction
import com.example.budgettracker.databinding.FragmentDeleteTransactionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * BottomSheetDialogFragment for confirming the deletion of a transaction.
 * This fragment displays the transaction details and allows the user to confirm or cancel the deletion.
 *
 * @param transaction The transaction to be deleted.
 */
class DeleteTransactionFragment(private val transaction: Transaction) : BottomSheetDialogFragment() {
    private var deleteTransactionBinding: FragmentDeleteTransactionBinding? = null
    private val binding get() = deleteTransactionBinding!!
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
        deleteTransactionBinding = FragmentDeleteTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called after the view has been created. Sets up click listeners for the delete and cancel buttons.
     *
     * @param view The created view for the fragment.
     * @param savedInstanceState Optional saved instance state for restoration.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.deleteBtn.setOnClickListener {
            // Delete the transaction and update the balance
            appViewModel.deleteTransaction(transaction)
            balanceUpdate(transaction.amount.toString(), transaction.type)
        }

        // Dismiss the dialog on cancel button click
        binding.cancelBtn.setOnClickListener { dismiss() }
    }

    /**
     * Updates the balance after a transaction is deleted.
     *
     * @param amount The amount of the deleted transaction.
     * @param type The type of the transaction (Credit or Debit).
     */
    private fun balanceUpdate(amount: String, type: String) {
        val transactionAmount = amount.toDoubleOrNull() ?: 0.0
        appViewModel.getBalance().observe(viewLifecycleOwner) { balance ->
            val currentBalance = balance?.availableBalance ?: 0.0

            // Calculate updated balance based on transaction type
            val updatedBalance = if (type == "Credit") {
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
     * @param updatedBalance The new balance after deletion.
     */
    private fun updateBalanceInViewModel(updatedBalance: Double) {
        val balance = Balance(id = 1, availableBalance = updatedBalance)
        appViewModel.updateBalance(balance)

        Toast.makeText(requireContext(), "Transaction deleted successfully", Toast.LENGTH_SHORT).show()

        dismiss() // Dismiss the dialog after updating the balance
    }
}
