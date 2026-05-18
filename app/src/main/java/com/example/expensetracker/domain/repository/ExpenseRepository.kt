package com.example.expensetracker.domain.repository

import com.example.expensetracker.data.local.CategoryTotal
import com.example.expensetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getAllExpenses(): Flow<List<Expense>>
    suspend fun addExpense(expense: Expense): Long
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(id: Long)
    fun getExpensesByTimeRange(startTime: Long, endTime: Long): Flow<List<Expense>>
    suspend fun getTotalExpenseByTimeRange(startTime: Long, endTime: Long): Double
    suspend fun getTotalByTypeAndTimeRange(type: String, startTime: Long, endTime: Long): Double
    suspend fun getCategoryTotals(startTime: Long, endTime: Long): List<CategoryTotal>
}
