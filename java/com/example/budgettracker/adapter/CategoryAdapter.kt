package com.example.budgettracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker.model.Category
import com.example.budgettracker.databinding.ItemLayoutBinding

/**
 * Adapter for displaying a list of categories in a RecyclerView.
 *
 * @property context The context in which the adapter is operating.
 * @property categoryList The list of categories to be displayed.
 * @property onCategoryClick A lambda function to handle click events on a category.
 */
class CategoryAdapter(
    val context: Context,
    private val categoryList: List<Category>,
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    /**
     * ViewHolder class that holds the view for each category item.
     *
     * @property binding The binding for the item layout.
     */
    class CategoryViewHolder(val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Creates new ViewHolder instances.
     *
     * @param parent The parent ViewGroup where the new View will be attached.
     * @param viewType The view type of the new View.
     * @return A new instance of CategoryViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        // Inflate the item layout using the binding class.
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    /**
     * Binds data to the ViewHolder for a specific position.
     *
     * @param holder The ViewHolder to bind data to.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        // Get the category at the current position.
        val category = categoryList[position]

        // Set the category name and icon in the ViewHolder.
        holder.binding.categoryName.text = category.name
        holder.binding.categoryIcon.setImageResource(category.icon)

        // Set an OnClickListener to handle category clicks.
        holder.itemView.setOnClickListener {
            onCategoryClick(category)
        }
    }

    /**
     * Returns the total number of items in the category list.
     *
     * @return The size of the categoryList.
     */
    override fun getItemCount(): Int = categoryList.size
}
