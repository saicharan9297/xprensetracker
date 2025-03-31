package com.example.budgettracker.ui

import com.example.budgettracker.adapter.CategoryAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker.model.Category
import com.example.budgettracker.R

/**
 * DialogFragment for selecting a category from a grid of options.
 * This fragment displays categories in a RecyclerView and returns the selected category to the calling activity or fragment.
 *
 * @param categories List of categories to be displayed.
 * @param onCategorySelected Callback function invoked when a category is selected.
 */
class CategorySelectionDialogFragment(
    private val categories: List<Category>,
    private val onCategorySelected: (Category) -> Unit
) : DialogFragment() {

    companion object {
        /**
         * Creates a new instance of CategorySelectionDialogFragment with the provided categories and selection callback.
         *
         * @param categories List of categories to display.
         * @param onCategorySelected Callback function to handle category selection.
         * @return An instance of CategorySelectionDialogFragment.
         */
        fun newInstance(
            categories: List<Category>,
            onCategorySelected: (Category) -> Unit
        ): CategorySelectionDialogFragment {
            return CategorySelectionDialogFragment(categories, onCategorySelected)
        }
    }

    /**
     * Inflates the dialog's layout and initializes the RecyclerView to display categories.
     *
     * @param inflater LayoutInflater to inflate the layout.
     * @param container Optional view group to contain the dialog view.
     * @param savedInstanceState Optional saved instance state for restoration.
     * @return The inflated view for the dialog.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for the dialog
        val view = inflater.inflate(R.layout.dialog_category_selection, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewCategories)

        // Set the LayoutManager for the RecyclerView to GridLayoutManager with 2 columns
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // Setup adapter with the provided categories
        recyclerView.adapter = CategoryAdapter(requireContext(), categories) { category ->
            // Handle category selection and dismiss the dialog
            onCategorySelected(category)
            dismiss() // Close the dialog after selection
        }

        return view // Return the inflated view
    }
}
