package com.example.expensetracker.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.ItemExpenseBinding
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.model.ExpenseCategory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExpenseAdapter(
    private val onEditClick: (Expense) -> Unit,
    private val onDeleteClick: (Expense) -> Unit
) : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ExpenseViewHolder(
        private val binding: ItemExpenseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        fun bind(expense: Expense) {
            val isIncome = expense.type == "INCOME"
            val incomeColor = android.graphics.Color.parseColor("#34C759")

            binding.apply {
                tvAmount.text = String.format(Locale.getDefault(), "%s¥%.2f", if (isIncome) "+" else "-", expense.amount)
                tvAmount.setTextColor(if (isIncome) incomeColor else android.graphics.Color.parseColor("#FF2D55"))
                tvCategory.text = expense.category
                tvNote.text = expense.note ?: ""
                tvDate.text = dateFormat.format(Date(expense.timestamp))

                val categoryEnum = ExpenseCategory.fromDisplayName(expense.category)
                tvCategory.setTextColor(if (isIncome) incomeColor else android.graphics.Color.parseColor(categoryEnum.colorHex))
                colorBar.setBackgroundColor(if (isIncome) incomeColor else android.graphics.Color.parseColor(categoryEnum.colorHex))

                btnEdit.setOnClickListener {
                    onEditClick(expense)
                }

                btnDelete.setOnClickListener {
                    onDeleteClick(expense)
                }
            }
        }
    }

    private class ExpenseDiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }
    }
}