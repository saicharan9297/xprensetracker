package com.example.budgettracker.adapter

import android.annotation.SuppressLint
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker.model.Transaction
import com.example.budgettracker.R
import com.example.budgettracker.databinding.TransactionLayoutBinding
import com.example.budgettracker.util.CurrencyFormatter
import java.text.SimpleDateFormat

/**
 * Adapter for displaying a list of transactions in a RecyclerView.
 *
 * This adapter binds the transaction data to the views in the transaction layout,
 * allowing for interaction and display of relevant information such as title,
 * category, amount, and timing.
 *
 * @param transactionList The list of transactions to be displayed.
 * @param onTransactionLongClick Lambda function to handle long click events on a transaction item.
 */
class TransactionAdapter(
    private val transactionList: List<Transaction>,
    private val onTransactionLongClick: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    /**
     * ViewHolder for each transaction item.
     *
     * @param itemBinding The binding object for the transaction layout.
     */
    class TransactionViewHolder(val itemBinding: TransactionLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    /**
     * Creates new ViewHolder instances for transaction items.
     *
     * @param parent The ViewGroup into which the new view will be added.
     * @param viewType The view type of the new view.
     * @return A new TransactionViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(
            TransactionLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    /**
     * Returns the total number of transactions.
     *
     * @return The size of the transaction list.
     */
    override fun getItemCount() = transactionList.size

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    /**
     * Binds the transaction data to the views in the ViewHolder.
     *
     * This method is responsible for displaying transaction details such as title,
     * amount, timing, and category icon. It also sets the appropriate formatting
     * and background colors based on the transaction type (Credit/Debit).
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the transaction in the list.
     */
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val red = ContextCompat.getColor(holder.itemView.context, R.color.red)
        val green = ContextCompat.getColor(holder.itemView.context, R.color.green)
        val currentTransaction = transactionList[position]

        // Set the transaction title
        holder.itemBinding.transactionTitle.text = currentTransaction.title

        // Handle long click events
        holder.itemView.setOnLongClickListener {
            onTransactionLongClick(currentTransaction)
            true
        }

        // Format and set the transaction timing
        val originalTiming = currentTransaction.transactionTiming
        val formattedTiming = DateFormat.format(
            "MMMM d, hh:mm a",
            SimpleDateFormat("yyyy MMMM d, HH:mm:ss a").parse(originalTiming)
        )
        holder.itemBinding.transactionTiming.text = formattedTiming.toString()

        // Set category icons based on the transaction category
        when (currentTransaction.category) {
            "Salary" -> holder.itemBinding.imageView.setImageResource(R.drawable.wages)
            "Gift & Awards" -> holder.itemBinding.imageView.setImageResource(R.drawable.award)
            "Rent" -> holder.itemBinding.imageView.setImageResource(R.drawable.rent)
            "Grocery" -> holder.itemBinding.imageView.setImageResource(R.drawable.grocery)
            "Milk" -> holder.itemBinding.imageView.setImageResource(R.drawable.milk)
            "Water" -> holder.itemBinding.imageView.setImageResource(R.drawable.drop)
            "Electricity" -> holder.itemBinding.imageView.setImageResource(R.drawable.electricity)
            "Phone Expenses" -> holder.itemBinding.imageView.setImageResource(R.drawable.bill)
            "Internet" -> holder.itemBinding.imageView.setImageResource(R.drawable.baseline_wifi_24)
            "Fuel" -> holder.itemBinding.imageView.setImageResource(R.drawable.fuel)
            "Gas" -> holder.itemBinding.imageView.setImageResource(R.drawable.gas_cylinder)
            "EMI" -> holder.itemBinding.imageView.setImageResource(R.drawable.money)
            "Insurance" -> holder.itemBinding.imageView.setImageResource(R.drawable.insurance)
            "Card Bill" -> holder.itemBinding.imageView.setImageResource(R.drawable.card_bill)
            "Food & Dining" -> holder.itemBinding.imageView.setImageResource(R.drawable.fastfood)
            "Entertainment" -> holder.itemBinding.imageView.setImageResource(R.drawable.entertainment)
            "Travel" -> holder.itemBinding.imageView.setImageResource(R.drawable.travel)
            "Clothing" -> holder.itemBinding.imageView.setImageResource(R.drawable.clothes)
            "Healthcare" -> holder.itemBinding.imageView.setImageResource(R.drawable.healthcare)
            "Personal Care" -> holder.itemBinding.imageView.setImageResource(R.drawable.personal_care)
            "Education" -> holder.itemBinding.imageView.setImageResource(R.drawable.education)
            "Investments" -> holder.itemBinding.imageView.setImageResource(R.drawable.investment)
            "Transportation" -> holder.itemBinding.imageView.setImageResource(R.drawable.transport)
            "Electronics" -> holder.itemBinding.imageView.setImageResource(R.drawable.electronics)
            "Subscriptions" -> holder.itemBinding.imageView.setImageResource(R.drawable.subscription_fee)
            "Donations" -> holder.itemBinding.imageView.setImageResource(R.drawable.donate)
            "Miscellaneous" -> holder.itemBinding.imageView.setImageResource(R.drawable.miscellaneous)
            "Cashback" -> holder.itemBinding.imageView.setImageResource(R.drawable.cashback)
            "Loan" -> holder.itemBinding.imageView.setImageResource(R.drawable.loan)
            "Investment Withdraw" -> holder.itemBinding.imageView.setImageResource(R.drawable.withdraw)
        }

        // Set transaction amount formatting based on transaction type
        when (currentTransaction.type) {
            "Credit" -> {
                holder.itemBinding.imageView.setBackgroundResource(R.drawable.green_box)
                holder.itemBinding.imageView.setColorFilter(green)
                holder.itemBinding.transactionAmount.setTextColor(green)
                holder.itemBinding.transactionAmount.text =
                    "+ ₹ " + CurrencyFormatter.formatIndianCurrency(currentTransaction.amount)
            }

            "Debit" -> {
                holder.itemBinding.imageView.setBackgroundResource(R.drawable.red_box)
                holder.itemBinding.imageView.setColorFilter(red)
                holder.itemBinding.transactionAmount.setTextColor(red)
                holder.itemBinding.transactionAmount.text =
                    "- ₹ " + CurrencyFormatter.formatIndianCurrency(currentTransaction.amount)
            }
        }
    }
}
