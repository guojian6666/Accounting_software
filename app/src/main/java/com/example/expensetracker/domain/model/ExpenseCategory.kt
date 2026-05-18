package com.example.expensetracker.domain.model

enum class ExpenseCategory(
    val displayName: String,
    val iconResName: String,
    val colorHex: String,
    val colorHexLight: String
) {
    FOOD("餐饮", "ic_category_food", "#FF6B35", "#FFD4BF"),
    SHOPPING("购物", "ic_category_shopping", "#FF2D55", "#FFCCD5"),
    TRANSPORT("交通", "ic_category_transport", "#5856D6", "#D1D0F5"),
    ENTERTAINMENT("娱乐", "ic_category_entertainment", "#34C759", "#C7F0D0"),
    MEDICAL("医疗", "ic_category_medical", "#FF3B30", "#FFCECD"),
    EDUCATION("教育", "ic_category_education", "#007AFF", "#B3D9FF"),
    HOUSING("居住", "ic_category_housing", "#FF9500", "#FFE0B3"),
    OTHER("其他", "ic_category_other", "#8E8E93", "#E5E5EA");

    companion object {
        fun fromDisplayName(name: String): ExpenseCategory {
            return entries.find { it.displayName == name } ?: OTHER
        }
    }
}