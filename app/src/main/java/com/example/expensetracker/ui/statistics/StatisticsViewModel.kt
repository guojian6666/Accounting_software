package com.example.expensetracker.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.local.CategoryTotal
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.model.StatisticsPeriod
import com.example.expensetracker.domain.usecase.GetExpenseStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getExpenseStatsUseCase: GetExpenseStatsUseCase
) : ViewModel() {

    private val _currentPeriod = MutableStateFlow(StatisticsPeriod.DAILY)
    val currentPeriod: StateFlow<StatisticsPeriod> = _currentPeriod.asStateFlow()

    private val _totalExpense = MutableStateFlow(0.0)
    val totalExpense: StateFlow<Double> = _totalExpense.asStateFlow()

    private val _categoryTotals = MutableStateFlow<List<CategoryTotal>>(emptyList())
    val categoryTotals: StateFlow<List<CategoryTotal>> = _categoryTotals.asStateFlow()

    private val _periodExpenses = MutableStateFlow<List<Expense>>(emptyList())
    val periodExpenses: StateFlow<List<Expense>> = _periodExpenses.asStateFlow()

    private val _currentDate = MutableStateFlow(System.currentTimeMillis())
    val currentDate: StateFlow<Long> = _currentDate.asStateFlow()

    private val _monthlyIncome = MutableStateFlow(0.0)
    val monthlyIncome: StateFlow<Double> = _monthlyIncome.asStateFlow()

    private val _monthlyExpense = MutableStateFlow(0.0)
    val monthlyExpense: StateFlow<Double> = _monthlyExpense.asStateFlow()

    private val _monthlyBalance = MutableStateFlow(0.0)
    val monthlyBalance: StateFlow<Double> = _monthlyBalance.asStateFlow()

    init {
        loadStatistics()
    }

    fun setPeriod(period: StatisticsPeriod) {
        _currentPeriod.value = period
        loadStatistics()
    }

    fun navigatePrevious() {
        val calendar = java.util.Calendar.getInstance().apply {
            timeInMillis = _currentDate.value
        }

        when (_currentPeriod.value) {
            StatisticsPeriod.DAILY -> calendar.add(java.util.Calendar.DAY_OF_MONTH, -1)
            StatisticsPeriod.WEEKLY -> calendar.add(java.util.Calendar.WEEK_OF_YEAR, -1)
            StatisticsPeriod.MONTHLY -> calendar.add(java.util.Calendar.MONTH, -1)
        }

        _currentDate.value = calendar.timeInMillis
        loadStatistics()
    }

    fun navigateNext() {
        val calendar = java.util.Calendar.getInstance().apply {
            timeInMillis = _currentDate.value
        }

        when (_currentPeriod.value) {
            StatisticsPeriod.DAILY -> calendar.add(java.util.Calendar.DAY_OF_MONTH, 1)
            StatisticsPeriod.WEEKLY -> calendar.add(java.util.Calendar.WEEK_OF_YEAR, 1)
            StatisticsPeriod.MONTHLY -> calendar.add(java.util.Calendar.MONTH, 1)
        }

        _currentDate.value = calendar.timeInMillis
        loadStatistics()
    }

    fun jumpToDate(timestamp: Long) {
        _currentDate.value = timestamp
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            val period = _currentPeriod.value
            val referenceTime = _currentDate.value

            launch {
                getExpenseStatsUseCase.getExpensesByPeriod(period, referenceTime).collect { expenses ->
                    _periodExpenses.value = expenses
                }
            }

            launch {
                val total = getExpenseStatsUseCase.getTotalExpenseByPeriod(period, referenceTime)
                _totalExpense.value = total
            }

            launch {
                val categoryTotals = getExpenseStatsUseCase.getCategoryTotalsByPeriod(period, referenceTime)
                _categoryTotals.value = categoryTotals
            }

            launch {
                val monthlyIncome = getExpenseStatsUseCase.getTotalIncomeByPeriod(StatisticsPeriod.MONTHLY, _currentDate.value)
                _monthlyIncome.value = monthlyIncome
                val monthlyExpense = getExpenseStatsUseCase.getTotalExpenseByPeriod(StatisticsPeriod.MONTHLY, _currentDate.value)
                _monthlyExpense.value = monthlyExpense
                _monthlyBalance.value = monthlyIncome - monthlyExpense
            }
        }
    }
}
