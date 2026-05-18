package com.example.expensetracker.data.local

import androidx.room.*
import com.example.expensetracker.data.local.entity.ExpenseRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseRecord): Long

    @Update
    suspend fun updateExpense(expense: ExpenseRecord)

    @Delete
    suspend fun deleteExpense(expense: ExpenseRecord)

    @Query("DELETE FROM expense_records WHERE id = :id")
    suspend fun deleteExpenseById(id: Long)

    @Query("SELECT * FROM expense_records ORDER BY timestamp DESC")
    fun getAllExpenses(): Flow<List<ExpenseRecord>>

    @Query("SELECT * FROM expense_records WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    fun getExpensesByTimeRange(startTime: Long, endTime: Long): Flow<List<ExpenseRecord>>

    @Query("SELECT * FROM expense_records WHERE category = :category ORDER BY timestamp DESC")
    fun getExpensesByCategory(category: String): Flow<List<ExpenseRecord>>

    @Query("SELECT SUM(amount) FROM expense_records WHERE timestamp BETWEEN :startTime AND :endTime")
    suspend fun getTotalExpenseByTimeRange(startTime: Long, endTime: Long): Double?

    @Query("SELECT SUM(amount) FROM expense_records WHERE type = :type AND timestamp BETWEEN :startTime AND :endTime")
    suspend fun getTotalByTypeAndTimeRange(type: String, startTime: Long, endTime: Long): Double?

    @Query("SELECT category, SUM(amount) as total FROM expense_records WHERE type = 'EXPENSE' AND timestamp BETWEEN :startTime AND :endTime GROUP BY category")
    suspend fun getCategoryTotals(startTime: Long, endTime: Long): List<CategoryTotal>
}

data class CategoryTotal(
    val category: String,
    val total: Double
)