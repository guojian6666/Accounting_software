package com.example.expensetracker.ui.home

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.R
import com.example.expensetracker.databinding.DialogAddExpenseBinding
import com.example.expensetracker.databinding.FragmentHomeBinding
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.model.ExpenseCategory
import com.example.expensetracker.domain.model.TransactionType
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var expenseAdapter: ExpenseAdapter
    private val dateFormat = SimpleDateFormat("yyyy年MM月dd日 EEEE", Locale.getDefault())
    private val todayFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFab()
        setupDateNavigation()
        observeData()
    }

    private fun setupRecyclerView() {
        expenseAdapter = ExpenseAdapter(
            onEditClick = { expense -> showEditExpenseDialog(expense) },
            onDeleteClick = { expense -> showDeleteConfirmation(expense) }
        )

        binding.rvExpenses.apply {
            adapter = expenseAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            showAddExpenseDialog()
        }
    }

    private fun setupDateNavigation() {
        binding.btnPreviousDay.setOnClickListener {
            viewModel.navigatePreviousDay()
        }

        binding.btnNextDay.setOnClickListener {
            viewModel.navigateNextDay()
        }

        binding.dateArea.setOnClickListener {
            val calendar = Calendar.getInstance().apply { timeInMillis = viewModel.selectedDate.value }
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    viewModel.jumpToDate(calendar.timeInMillis)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.expenses.collect { expenses ->
                        expenseAdapter.submitList(expenses)
                        binding.tvEmpty.visibility = if (expenses.isEmpty()) View.VISIBLE else View.GONE
                        binding.rvExpenses.visibility = if (expenses.isEmpty()) View.GONE else View.VISIBLE
                    }
                }

                launch {
                    viewModel.selectedDate.collect { date ->
                        updateDateTitle(date)
                    }
                }

                launch {
                    viewModel.totalExpense.collect { total ->
                        val isToday = todayFormat.format(System.currentTimeMillis()) == todayFormat.format(viewModel.selectedDate.value)
                        val label = if (isToday) "今日支出" else "当日支出"
                        binding.tvTotalExpense.text = String.format(Locale.getDefault(), "$label ¥%.2f", total)
                    }
                }

                launch {
                    viewModel.totalIncome.collect { total ->
                        val isToday = todayFormat.format(System.currentTimeMillis()) == todayFormat.format(viewModel.selectedDate.value)
                        val label = if (isToday) "今日收入" else "当日收入"
                        binding.tvTotalIncome.text = String.format(Locale.getDefault(), "$label ¥%.2f", total)
                    }
                }
            }
        }
    }

    private fun updateDateTitle(timestamp: Long) {
        val dateStr = dateFormat.format(Date(timestamp))
        val todayStr = todayFormat.format(System.currentTimeMillis())
        val selectedStr = todayFormat.format(timestamp)

        if (todayStr == selectedStr) {
            binding.tvDateTitle.text = "今天 ($dateStr)"
        } else {
            binding.tvDateTitle.text = dateStr
        }
    }

    private fun showAddExpenseDialog() {
        showExpenseDialog(null)
    }

    private fun showEditExpenseDialog(expense: Expense) {
        showExpenseDialog(expense)
    }

    private fun showExpenseDialog(existingExpense: Expense?) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogAddExpenseBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val isEdit = existingExpense != null
        val calendar = Calendar.getInstance()
        val dateDisplayFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())

        var selectedTimestamp = if (isEdit) {
            calendar.timeInMillis = existingExpense!!.timestamp
            existingExpense.timestamp
        } else {
            System.currentTimeMillis()
        }
        calendar.timeInMillis = selectedTimestamp

        if (isEdit) {
            val isIncome = existingExpense!!.type == "INCOME"
            dialogBinding.toggleType.check(if (isIncome) R.id.btn_type_income else R.id.btn_type_expense)
        }

        setupCategoryChips(dialogBinding, existingExpense?.category)

        val updateDateField: () -> Unit = {
            dialogBinding.etDate.setText(dateDisplayFormat.format(Date(selectedTimestamp)))
        }
        updateDateField()

        dialogBinding.etDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    calendar.set(Calendar.HOUR_OF_DAY, 12)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    selectedTimestamp = calendar.timeInMillis
                    updateDateField()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        if (isEdit) {
            dialogBinding.tvDialogTitle.text = getString(R.string.edit_expense)
            dialogBinding.etAmount.setText(String.format(Locale.getDefault(), "%.2f", existingExpense!!.amount))
            dialogBinding.etNote.setText(existingExpense.note ?: "")
        }

        setupDialogButtons(dialog, dialogBinding, existingExpense) { selectedTimestamp }

        dialog.show()
    }

    private fun setupCategoryChips(dialogBinding: DialogAddExpenseBinding, selectedCategory: String?) {
        dialogBinding.chipGroupCategories.removeAllViews()

        ExpenseCategory.entries.forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = category.displayName
                isCheckable = true
                isChecked = category.displayName == selectedCategory

                val mainColor = Color.parseColor(category.colorHex)
                val lightColor = Color.parseColor(category.colorHexLight)

                chipBackgroundColor = ColorStateList(
                    arrayOf(
                        intArrayOf(android.R.attr.state_checked),
                        intArrayOf(-android.R.attr.state_checked)
                    ),
                    intArrayOf(mainColor, lightColor)
                )

                setTextColor(
                    ColorStateList(
                        arrayOf(
                            intArrayOf(android.R.attr.state_checked),
                            intArrayOf(-android.R.attr.state_checked)
                        ),
                        intArrayOf(Color.WHITE, mainColor)
                    )
                )

                chipStrokeColor = ColorStateList(
                    arrayOf(
                        intArrayOf(android.R.attr.state_checked),
                        intArrayOf(-android.R.attr.state_checked)
                    ),
                    intArrayOf(mainColor, mainColor)
                )
                chipStrokeWidth = 1f

                tag = category.displayName
            }
            dialogBinding.chipGroupCategories.addView(chip)
        }
    }

    private fun setupDialogButtons(dialog: Dialog, dialogBinding: DialogAddExpenseBinding, existingExpense: Expense?, getTimestamp: () -> Long) {
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnSave.setOnClickListener {
            val amountStr = dialogBinding.etAmount.text.toString()
            val note = dialogBinding.etNote.text.toString()

            if (amountStr.isBlank()) {
                Toast.makeText(requireContext(), R.string.error_empty_amount, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountStr.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                Toast.makeText(requireContext(), R.string.error_invalid_amount, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedChipId = dialogBinding.chipGroupCategories.checkedChipId
            if (selectedChipId == View.NO_ID) {
                Toast.makeText(requireContext(), R.string.error_select_category, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedChip = dialogBinding.chipGroupCategories.findViewById<Chip>(selectedChipId)
            val category = selectedChip.tag as String
            val type = if (dialogBinding.toggleType.checkedButtonId == R.id.btn_type_income) "INCOME" else "EXPENSE"

            if (existingExpense != null) {
                val updatedExpense = existingExpense.copy(
                    amount = amount,
                    category = category,
                    note = note.takeIf { it.isNotBlank() },
                    timestamp = getTimestamp(),
                    type = type
                )
                viewModel.updateExpense(updatedExpense)
            } else {
                viewModel.addExpense(amount, category, note.takeIf { it.isNotBlank() }, getTimestamp(), type)
            }

            dialog.dismiss()
        }
    }

    private fun showDeleteConfirmation(expense: Expense) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(R.string.confirm_delete)
            .setMessage(R.string.confirm_delete_message)
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteExpense(expense.id)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}