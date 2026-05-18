package com.example.expensetracker.ui.statistics

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.expensetracker.R
import com.example.expensetracker.databinding.FragmentStatisticsBinding
import com.example.expensetracker.domain.model.Expense
import com.example.expensetracker.domain.model.ExpenseCategory
import com.example.expensetracker.domain.model.StatisticsPeriod
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StatisticsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCharts()
        setupPeriodTabs()
        setupNavigation()
        observeData()
    }

    private fun setupCharts() {
        setupBarChart()
        setupPieChart()
    }

    private fun setupBarChart() {
        binding.barChart.apply {
            description.isEnabled = false
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            setDrawValueAboveBar(true)
            setPinchZoom(false)
            setScaleEnabled(false)
            setExtraOffsets(8f, 8f, 16f, 8f)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                textColor = ContextCompat.getColor(requireContext(), R.color.text_secondary)
                textSize = 10f
                labelRotationAngle = -45f
            }

            axisLeft.apply {
                setDrawGridLines(true)
                textColor = ContextCompat.getColor(requireContext(), R.color.text_secondary)
                axisMinimum = 0f
                granularity = 0.1f
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return if (value == 0f) "0"
                        else String.format(Locale.getDefault(), "%.2f", value)
                    }
                }
            }

            axisRight.isEnabled = false

            legend.apply {
                isEnabled = false
            }

            animateY(500)
        }
    }

    private fun setupPieChart() {
        binding.pieChart.apply {
            description.isEnabled = false
            setUsePercentValues(true)
            setDrawEntryLabels(false)
            setDrawCenterText(true)
            centerText = getString(R.string.total_expense)
            setCenterTextSize(14f)
            setCenterTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))

            holeRadius = 45f
            transparentCircleRadius = 50f
            setHoleColor(Color.WHITE)

            legend.apply {
                isEnabled = false
            }

            animateXY(500, 500)
        }
    }

    private fun setupPeriodTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                val period = when (tab?.position) {
                    0 -> StatisticsPeriod.DAILY
                    1 -> StatisticsPeriod.WEEKLY
                    2 -> StatisticsPeriod.MONTHLY
                    else -> StatisticsPeriod.DAILY
                }
                viewModel.setPeriod(period)
            }

            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })
    }

    private fun setupNavigation() {
        binding.btnPrevious.setOnClickListener {
            viewModel.navigatePrevious()
        }

        binding.btnNext.setOnClickListener {
            viewModel.navigateNext()
        }

        binding.dateArea.setOnClickListener {
            val calendar = Calendar.getInstance().apply { timeInMillis = viewModel.currentDate.value }
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
                    viewModel.currentPeriod.collect { period ->
                        updatePeriodTitle(period)
                    }
                }

                launch {
                    viewModel.currentDate.collect { _ ->
                        updateDateTitle()
                    }
                }

                launch {
                    viewModel.totalExpense.collect { total ->
                        binding.tvTotalAmount.text = String.format(Locale.getDefault(), "¥%.2f", total)
                    }
                }

                launch {
                    viewModel.categoryTotals.collect { categoryTotals ->
                        updatePieChart(categoryTotals)
                    }
                }

                launch {
                    viewModel.periodExpenses.collect { expenses ->
                        updateBarChart(expenses)
                    }
                }

                launch {
                    viewModel.monthlyBalance.collect { balance ->
                        binding.tvBalance.text = String.format(Locale.getDefault(), "¥%.2f", balance)
                        if (balance >= 0) {
                            binding.tvBalance.setTextColor(ContextCompat.getColor(requireContext(), R.color.category_entertainment))
                        } else {
                            binding.tvBalance.setTextColor(ContextCompat.getColor(requireContext(), R.color.category_food))
                        }
                    }
                }

                launch {
                    viewModel.monthlyIncome.collect { income ->
                        binding.tvBalanceIncome.text = String.format(Locale.getDefault(), "收入 ¥%.2f", income)
                    }
                }

                launch {
                    viewModel.monthlyExpense.collect { expense ->
                        binding.tvBalanceExpense.text = String.format(Locale.getDefault(), "支出 ¥%.2f", expense)
                    }
                }
            }
        }
    }

    private fun updatePeriodTitle(period: StatisticsPeriod) {
        binding.tvPeriodTitle.text = when (period) {
            StatisticsPeriod.DAILY -> getString(R.string.statistics_daily)
            StatisticsPeriod.WEEKLY -> getString(R.string.statistics_weekly)
            StatisticsPeriod.MONTHLY -> getString(R.string.statistics_monthly)
        }
    }

    private fun updateDateTitle() {
        val dateStr = when (viewModel.currentPeriod.value) {
            StatisticsPeriod.DAILY -> SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault()).format(viewModel.currentDate.value)
            StatisticsPeriod.WEEKLY -> {
                val cal = Calendar.getInstance().apply { timeInMillis = viewModel.currentDate.value }
                val month = cal.get(Calendar.MONTH) + 1
                "第${cal.get(Calendar.WEEK_OF_MONTH)}周 (${month}月)"
            }
            StatisticsPeriod.MONTHLY -> SimpleDateFormat("yyyy年MM月", Locale.getDefault()).format(viewModel.currentDate.value)
        }
        binding.tvCurrentDate.text = dateStr
    }

    private fun updateBarChart(expenses: List<Expense>) {
        if (expenses.isEmpty()) {
            binding.barChart.clear()
            binding.barChart.invalidate()
            return
        }

        binding.barChart.xAxis.labelRotationAngle = -45f

        val groupedExpenses: List<BarEntry>
        val labels: List<String>

        when (viewModel.currentPeriod.value) {
            StatisticsPeriod.DAILY -> {
                val sorted = expenses.sortedByDescending { it.amount }
                groupedExpenses = sorted.mapIndexed { index, expense ->
                    BarEntry(index.toFloat(), expense.amount.toFloat())
                }
                labels = sorted.map { expense ->
                    "${expense.category} ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(java.util.Date(expense.timestamp))}"
                }
                binding.barChart.xAxis.labelRotationAngle = -60f
            }
            StatisticsPeriod.WEEKLY -> {
                val dayExpenses = mutableMapOf<Int, Double>()

                expenses.forEach { expense ->
                    val expenseCal = Calendar.getInstance().apply { timeInMillis = expense.timestamp }
                    val dayOfWeek = expenseCal.get(Calendar.DAY_OF_WEEK)
                    dayExpenses[dayOfWeek] = (dayExpenses[dayOfWeek] ?: 0.0) + expense.amount
                }

                val dayOrder = intArrayOf(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY,
                    Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY)
                labels = listOf("周一", "周二", "周三", "周四", "周五", "周六", "周日")
                groupedExpenses = dayOrder.mapIndexed { index, day ->
                    BarEntry(index.toFloat(), dayExpenses[day]?.toFloat() ?: 0f)
                }
                binding.barChart.xAxis.labelRotationAngle = 0f
            }
            StatisticsPeriod.MONTHLY -> {
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = viewModel.currentDate.value
                }

                val dayExpenses = mutableMapOf<Int, Double>()
                expenses.forEach { expense ->
                    val expenseCal = Calendar.getInstance().apply { timeInMillis = expense.timestamp }
                    val dayOfMonth = expenseCal.get(Calendar.DAY_OF_MONTH)
                    dayExpenses[dayOfMonth] = (dayExpenses[dayOfMonth] ?: 0.0) + expense.amount
                }

                val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                labels = (1..maxDay).map { "${it}日" }
                groupedExpenses = (1..maxDay).map {
                    BarEntry(it.toFloat(), dayExpenses[it]?.toFloat() ?: 0f)
                }
                binding.barChart.xAxis.labelRotationAngle = -45f
            }
        }

        binding.barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)

        val dataSet = BarDataSet(groupedExpenses, "消费").apply {
            color = ContextCompat.getColor(requireContext(), R.color.primary)
            valueTextColor = ContextCompat.getColor(requireContext(), R.color.text_secondary)
            valueTextSize = 10f
            setDrawValues(groupedExpenses.size <= 20)
        }

        val barWidth: Float
        val maxVisible: Float

        when (viewModel.currentPeriod.value) {
            StatisticsPeriod.DAILY -> {
                barWidth = 0.6f
                maxVisible = 10f
                binding.barChart.xAxis.axisMinimum = -0.5f
                binding.barChart.xAxis.axisMaximum = groupedExpenses.size.toFloat() + 0.5f
            }
            StatisticsPeriod.WEEKLY -> {
                barWidth = 0.3f
                maxVisible = 8f
                binding.barChart.xAxis.axisMinimum = -0.8f
                binding.barChart.xAxis.axisMaximum = 6.8f
            }
            StatisticsPeriod.MONTHLY -> {
                barWidth = 0.6f
                maxVisible = 14f
                binding.barChart.xAxis.axisMinimum = 0.5f
                binding.barChart.xAxis.axisMaximum = groupedExpenses.size.toFloat() + 0.5f
            }
        }

        binding.barChart.setVisibleXRangeMaximum(maxVisible)
        binding.barChart.data = BarData(dataSet).apply { this.barWidth = barWidth }
        binding.barChart.moveViewToX(0f)
        binding.barChart.invalidate()
    }

    private fun updatePieChart(categoryTotals: List<com.example.expensetracker.data.local.CategoryTotal>) {
        binding.categoryBreakdown.removeAllViews()

        if (categoryTotals.isEmpty()) {
            binding.pieChart.clear()
            binding.pieChart.invalidate()
            return
        }

        val sorted = categoryTotals.sortedByDescending { it.total }
        val grandTotal = sorted.sumOf { it.total }

        val entries = sorted.map { categoryTotal ->
            PieEntry(categoryTotal.total.toFloat(), categoryTotal.category)
        }

        val colors = sorted.map { categoryTotal ->
            val category = ExpenseCategory.fromDisplayName(categoryTotal.category)
            Color.parseColor(category.colorHex)
        }

        val dataSet = PieDataSet(entries, "").apply {
            this.colors = colors
            setDrawValues(false)
            sliceSpace = 2f
        }

        binding.pieChart.data = PieData(dataSet)
        binding.pieChart.centerText = getString(R.string.total_expense)
        binding.pieChart.invalidate()

        val inflater = LayoutInflater.from(requireContext())
        sorted.forEach { item ->
            val row = inflater.inflate(R.layout.item_category_breakdown, binding.categoryBreakdown, false)
            val dot = row.findViewById<View>(R.id.color_dot)
            val tvName = row.findViewById<TextView>(R.id.tv_category_name)
            val tvAmount = row.findViewById<TextView>(R.id.tv_category_amount)

            val category = ExpenseCategory.fromDisplayName(item.category)
            dot.setBackgroundColor(Color.parseColor(category.colorHex))
            tvName.text = item.category
            val pct = if (grandTotal > 0) item.total / grandTotal * 100 else 0.0
            tvAmount.text = String.format(Locale.getDefault(), "¥%.2f  (%.1f%%)", item.total, pct)

            binding.categoryBreakdown.addView(row)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}