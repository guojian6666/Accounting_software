package com.example.expensetracker.domain.usecase

import com.example.expensetracker.data.local.CategoryTotal
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.model.StatisticsPeriod
import com.example.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject

class GetExpenseStatsUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    fun getExpensesByPeriod(period: StatisticsPeriod, referenceTime: Long = System.currentTimeMillis()): Flow<List<Expense>> {
        val (startTime, endTime) = getTimeRange(period, referenceTime)
        return repository.getExpensesByTimeRange(startTime, endTime)
    }

    suspend fun getTotalExpenseByPeriod(period: StatisticsPeriod, referenceTime: Long = System.currentTimeMillis()): Double {
        val (startTime, endTime) = getTimeRange(period, referenceTime)
        return repository.getTotalByTypeAndTimeRange("EXPENSE", startTime, endTime)
    }

    suspend fun getTotalIncomeByPeriod(period: StatisticsPeriod, referenceTime: Long = System.currentTimeMillis()): Double {
        val (startTime, endTime) = getTimeRange(period, referenceTime)
        return repository.getTotalByTypeAndTimeRange("INCOME", startTime, endTime)
    }

    suspend fun getCategoryTotalsByPeriod(period: StatisticsPeriod, referenceTime: Long = System.currentTimeMillis()): List<CategoryTotal> {
        val (startTime, endTime) = getTimeRange(period, referenceTime)
        return repository.getCategoryTotals(startTime, endTime)
    }

    fun getTimeRange(period: StatisticsPeriod, referenceTime: Long): Pair<Long, Long> {
        val calendar = Calendar.getInstance().apply { timeInMillis = referenceTime }

        return when (period) {
            StatisticsPeriod.DAILY -> {
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val startOfDay = calendar.timeInMillis

                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                calendar.set(Calendar.SECOND, 59)
                calendar.set(Calendar.MILLISECOND, 999)
                val endOfDay = calendar.timeInMillis

                Pair(startOfDay, endOfDay)
            }

            StatisticsPeriod.WEEKLY -> {
                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val startOfWeek = calendar.timeInMillis

                calendar.add(Calendar.WEEK_OF_YEAR, 1)
                calendar.add(Calendar.MILLISECOND, -1)
                val endOfWeek = calendar.timeInMillis

                Pair(startOfWeek, endOfWeek)
            }

            StatisticsPeriod.MONTHLY -> {
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val startOfMonth = calendar.timeInMillis

                calendar.add(Calendar.MONTH, 1)
                calendar.add(Calendar.MILLISECOND, -1)
                val endOfMonth = calendar.timeInMillis

                Pair(startOfMonth, endOfMonth)
            }
        }
    }
}
