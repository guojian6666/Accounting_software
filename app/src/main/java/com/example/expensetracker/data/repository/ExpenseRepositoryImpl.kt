package com.example.expensetracker.data.repository

import com.example.expensetracker.data.local.CategoryTotal
import com.example.expensetracker.data.local.ExpenseDao
import com.example.expensetracker.data.local.entity.ExpenseRecord
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {

    override fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses().map { records ->
            records.map { it.toDomain() }
        }
    }

    override suspend fun addExpense(expense: Expense): Long {
        return expenseDao.insertExpense(expense.toEntity())
    }

    override suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense.toEntity())
    }

    override suspend fun deleteExpense(id: Long) {
        expenseDao.deleteExpenseById(id)
    }

    override fun getExpensesByTimeRange(startTime: Long, endTime: Long): Flow<List<Expense>> {
        return expenseDao.getExpensesByTimeRange(startTime, endTime).map { records ->
            records.map { it.toDomain() }
        }
    }

    override suspend fun getTotalExpenseByTimeRange(startTime: Long, endTime: Long): Double {
        return expenseDao.getTotalExpenseByTimeRange(startTime, endTime) ?: 0.0
    }

    override suspend fun getTotalByTypeAndTimeRange(type: String, startTime: Long, endTime: Long): Double {
        return expenseDao.getTotalByTypeAndTimeRange(type, startTime, endTime) ?: 0.0
    }

    override suspend fun getCategoryTotals(startTime: Long, endTime: Long): List<CategoryTotal> {
        return expenseDao.getCategoryTotals(startTime, endTime)
    }

    private fun ExpenseRecord.toDomain(): Expense {
        return Expense(
            id = id,
            amount = amount,
            category = category,
            note = note,
            timestamp = timestamp,
            type = type
        )
    }

    private fun Expense.toEntity(): ExpenseRecord {
        return ExpenseRecord(
            id = id,
            amount = amount,
            category = category,
            note = note,
            timestamp = timestamp,
            type = type
        )
    }
}
