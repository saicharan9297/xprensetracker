package com.example.budgettracker.ui

import com.example.budgettracker.viewmodel.AppViewModelFactory
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgettracker.repo.AppRepo
import com.example.budgettracker.viewmodel.AppViewModel
import com.example.budgettracker.db.AppDB
import com.example.budgettracker.adapter.TransactionAdapter
import com.example.budgettracker.R
import com.example.budgettracker.databinding.FragmentTransactionBinding
import com.example.budgettracker.ui.bottom_sheets.DeleteTransactionFragment
import com.example.budgettracker.util.CurrencyFormatter

/**
 * A Fragment class that displays all the transactions and handles filtering by month.
 */
class TransactionFragment : Fragment() {
    // View binding for FragmentTransaction
    private var transactionBinding: FragmentTransactionBinding? = null
    private val binding get() = transactionBinding!!

    // ViewModel instance for managing data
    private lateinit var appViewModel: AppViewModel

    // Variable to track selected month filter
    private var monthFilter: String = "All"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using View Binding
        transactionBinding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        val appDB = AppDB(requireContext())
        val appRepo = AppRepo(appDB)
        val viewModelFactory = AppViewModelFactory(requireActivity().application, appRepo)
        appViewModel = ViewModelProvider(this, viewModelFactory)[AppViewModel::class.java]

        // Set default selected button to "All Transactions"
        binding.AllTransactionBtn.setBackgroundResource(R.drawable.black_box)

        // Display all transactions by default
        allTransactionView()

        // Set up month filter buttons
        monthFilterBtn()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        transactionBinding = null // Avoid memory leaks by clearing the binding reference
    }

    /**
     * Sets up the functionality for the month filter buttons.
     */
    private fun monthFilterBtn() {
        val purpleColor = ContextCompat.getColor(requireContext(), R.color.purple_500)
        val blackColor = ContextCompat.getColor(requireContext(), R.color.black)

        // List of buttons corresponding to each month
        val monthButtons = listOf(
            binding.janBtn to "January",
            binding.febBtn to "February",
            binding.marBtn to "March",
            binding.aprBtn to "April",
            binding.mayBtn to "May",
            binding.junBtn to "June",
            binding.julBtn to "July",
            binding.augBtn to "August",
            binding.sepBtn to "September",
            binding.octBtn to "October",
            binding.novBtn to "November",
            binding.decBtn to "December"
        )

        // Function to reset all button colors to default
        fun resetButtonColors() {
            binding.AllTransactionBtn.setBackgroundResource(R.drawable.purple_box)
            monthButtons.forEach { it.first.setBackgroundColor(purpleColor) }
        }

        // Set up the "All Transactions" button
        binding.AllTransactionBtn.setOnClickListener {
            monthFilter = "All"
            allTransactionView() // Display all transactions
            binding.AllTransactionBtn.setBackgroundResource(R.drawable.black_box)
            monthButtons.forEach { it.first.setBackgroundColor(purpleColor) }
            binding.drCrBox.visibility = View.GONE
        }

        // Set up each month button
        monthButtons.forEach { (button, monthName) ->
            button.setOnClickListener {
                monthFilter = monthName.substring(0, 3) // Set the filter to the selected month
                monthFilterView(monthName) // Display transactions for the selected month
                resetButtonColors()
                button.setBackgroundColor(blackColor)
                binding.drCrBox.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Displays all transactions when "All Transactions" is selected.
     */
    private fun allTransactionView() {
        appViewModel.getAllTransaction().observe(viewLifecycleOwner) { transactionList ->
            if (transactionList.isNullOrEmpty()) {
                binding.emptyTxt.visibility = View.VISIBLE
                binding.homeRecyclerView.visibility = View.GONE
            } else {
                binding.emptyTxt.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
                binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.homeRecyclerView.adapter = TransactionAdapter(
                    transactionList
                ) { transaction ->
                    val bottomSheetFragment = DeleteTransactionFragment(transaction)
                    bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
                }
            }
        }
    }

    /**
     * Displays transactions for a selected month and calculates the total income and expense.
     * @param month The selected month.
     */
    @SuppressLint("SetTextI18n")
    private fun monthFilterView(month: String) {
        appViewModel.searchTransactionByMonth(month)
            .observe(viewLifecycleOwner) { transactionList ->

                if (transactionList.isNullOrEmpty()) {
                    binding.emptyTxt.visibility = View.VISIBLE
                    binding.homeRecyclerView.visibility = View.GONE
                    binding.drCrBox.visibility = View.GONE
                } else {
                    binding.emptyTxt.visibility = View.GONE
                    binding.homeRecyclerView.visibility = View.VISIBLE
                    binding.drCrBox.visibility = View.VISIBLE
                    binding.homeRecyclerView.layoutManager =
                        LinearLayoutManager(requireContext())
                    binding.homeRecyclerView.adapter = TransactionAdapter(
                        transactionList
                    ) { transaction ->
                        val bottomSheetFragment = DeleteTransactionFragment(transaction)
                        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
                    }
                }

                // Calculate total income and expense for the selected month
                var income = 0.0
                var expense = 0.0

                transactionList.forEach { transaction ->
                    if (transaction.type == "Credit") {
                        income += transaction.amount
                    } else if (transaction.type == "Debit") {
                        expense += transaction.amount
                    }
                }

                // Display formatted income and expense values
                binding.incomeTxt.text =
                    " ₹ ${CurrencyFormatter.formatIndianCurrency(income)}"
                binding.expenseTxt.text =
                    "₹ ${CurrencyFormatter.formatIndianCurrency(expense)}"
            }
    }
}
