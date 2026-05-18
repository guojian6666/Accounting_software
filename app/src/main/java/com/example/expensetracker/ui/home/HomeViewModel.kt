package com.example.expensetracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.model.TransactionType
import com.example.expensetracker.domain.repository.ExpenseRepository
import com.example.expensetracker.domain.usecase.AddExpenseUseCase
import com.example.expensetracker.domain.usecase.DeleteExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ExpenseRepository,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase
) : ViewModel() {

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedDate = MutableStateFlow(System.currentTimeMillis())
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()

    private val _totalExpense = MutableStateFlow(0.0)
    val totalExpense: StateFlow<Double> = _totalExpense.asStateFlow()

    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome.asStateFlow()

    init {
        loadExpensesByDate()
    }

    fun navigatePreviousDay() {
        val calendar = Calendar.getInstance().apply { timeInMillis = _selectedDate.value }
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        _selectedDate.value = calendar.timeInMillis
        loadExpensesByDate()
    }

    fun navigateNextDay() {
        val calendar = Calendar.getInstance().apply { timeInMillis = _selectedDate.value }
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        _selectedDate.value = calendar.timeInMillis
        loadExpensesByDate()
    }

    fun jumpToToday() {
        _selectedDate.value = System.currentTimeMillis()
        loadExpensesByDate()
    }

    fun jumpToDate(timestamp: Long) {
        _selectedDate.value = timestamp
        loadExpensesByDate()
    }

    private fun loadExpensesByDate() {
        viewModelScope.launch {
            val (start, end) = getDayRange(_selectedDate.value)
            repository.getExpensesByTimeRange(start, end).collect { expenseList ->
                _expenses.value = expenseList
                _totalExpense.value = expenseList.filter { it.type == "EXPENSE" }.sumOf { it.amount }
                _totalIncome.value = expenseList.filter { it.type == "INCOME" }.sumOf { it.amount }
            }
        }
    }

    private fun getDayRange(timestamp: Long): Pair<Long, Long> {
        val calendar = Calendar.getInstance().apply { timeInMillis = timestamp }
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val start = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val end = calendar.timeInMillis
        return Pair(start, end)
    }

    fun addExpense(amount: Double, category: String, note: String?, timestamp: Long = System.currentTimeMillis(), type: String = "EXPENSE") {
        viewModelScope.launch {
            _isLoading.value = true
            addExpenseUseCase(amount, category, note, timestamp, type)
            _isLoading.value = false
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            repository.updateExpense(expense)
        }
    }

    fun deleteExpense(id: Long) {
        viewModelScope.launch {
            deleteExpenseUseCase(id)
        }
    }
}