package com.example.expensetracker.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Expense(
    val id: Long = 0,
    val amount: Double,
    val category: String,
    val note: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "EXPENSE"
) : Parcelable