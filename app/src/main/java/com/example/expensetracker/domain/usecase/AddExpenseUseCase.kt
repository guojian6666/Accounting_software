package com.example.expensetracker.domain.usecase

import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.repository.ExpenseRepository
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(
        amount: Double,
        category: String,
        note: String?,
        timestamp: Long = System.currentTimeMillis(),
        type: String = "EXPENSE"
    ): Long {
        val expense = Expense(
            amount = amount,
            category = category,
            note = note?.takeIf { it.isNotBlank() },
            timestamp = timestamp,
            type = type
        )
        return repository.addExpense(expense)
    }
}
