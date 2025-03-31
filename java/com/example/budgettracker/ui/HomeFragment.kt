package com.example.budgettracker.ui

import android.content.ContentValues.TAG
import com.example.budgettracker.viewmodel.AppViewModelFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgettracker.repo.AppRepo
import com.example.budgettracker.viewmodel.AppViewModel
import com.example.budgettracker.db.AppDB
import com.example.budgettracker.adapter.TransactionAdapter
import com.example.budgettracker.databinding.FragmentHomeBinding
import com.example.budgettracker.ui.bottom_sheets.AddTransactionFragment
import com.example.budgettracker.ui.bottom_sheets.DeleteTransactionFragment
import com.example.budgettracker.util.CurrencyFormatter

/**
 * HomeFragment displays the latest transactions and the available balance.
 * It also provides an option to add new transactions using a floating action button.
 */
class HomeFragment : Fragment() {

    // Binding object instance corresponding to the fragment_home.xml layout
    private var homeBinding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = homeBinding!!

    // ViewModel instance to interact with the data layer
    private lateinit var appViewModel: AppViewModel

    /**
     * Inflates the fragment layout and initializes the binding
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using data binding
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called immediately after onCreateView; sets up the ViewModel, observes data, and handles UI interactions.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the database, repository, and ViewModel
        val appDB = AppDB(requireContext())
        val appRepo = AppRepo(appDB)
        val viewModelFactory = AppViewModelFactory(requireActivity().application, appRepo)
        appViewModel = ViewModelProvider(this, viewModelFactory)[AppViewModel::class.java]

        // Observe available balance and update the UI
        appViewModel.getBalance().observe(viewLifecycleOwner) { balance ->
            binding.TxtAvailableBalance.text = balance?.let {
                CurrencyFormatter.formatIndianCurrency(it.availableBalance)
            } ?: "0"
        }

        // Set up click listener for adding new transactions
        binding.addTransactionFab.setOnClickListener {
            // Show the AddTransactionFragment as a bottom sheet
            val bottomSheetFragment = AddTransactionFragment()
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }

        // Display the list of transactions
        allTransactionView()
    }

    /**
     * Observes the list of transactions from the ViewModel and displays them in the RecyclerView.
     */
    private fun allTransactionView() {
        // Observe the latest transactions and update the RecyclerView accordingly
        appViewModel.getLatestTransactions().observe(viewLifecycleOwner) { transactionList ->
            if (transactionList.isNullOrEmpty()) {
                // Show empty text and hide RecyclerView when there are no transactions
                binding.emptyTxt.visibility = View.VISIBLE
                binding.homeRecyclerView.visibility = View.GONE
            } else {
                // Hide empty text and show RecyclerView with the transaction list
                binding.emptyTxt.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
                binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.homeRecyclerView.adapter = TransactionAdapter(
                    transactionList
                ) { transaction ->
                    // Show the DeleteTransactionFragment as a bottom sheet for the selected transaction
                    val bottomSheetFragment = DeleteTransactionFragment(transaction)
                    bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
                }
            }
        }
    }

    /**
     * Called when the fragment's view is destroyed; ensures the binding reference is set to null to avoid memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        homeBinding = null // Avoid memory leaks
    }
}
