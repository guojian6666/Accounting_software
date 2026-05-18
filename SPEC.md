# 安卓记账软件 - 产品规范文档

## 1. 项目概述

**项目名称**: ExpenseTracker（记账助手）

**核心功能**: 一款简洁易用的安卓记账应用，支持消费记录的添加、删除、分类管理，以及按日/周/月统计消费情况，并提供直观的图表展示。

**目标用户**: 需要日常记录个人消费、控制支出的安卓用户。

## 2. 技术栈选择

- **开发语言**: Kotlin
- **最低SDK版本**: 24 (Android 7.0)
- **目标SDK版本**: 34 (Android 14)
- **架构模式**: MVVM + Clean Architecture
- **依赖注入**: Hilt
- **数据库**: Room
- **异步处理**: Kotlin Coroutines + Flow
- **图表库**: MPAndroidChart
- **UI框架**: Material Design 3 + View Binding

## 3. 功能列表

### 3.1 消费记录管理
- **添加消费记录**
  - 输入消费金额（必填）
  - 选择消费类别（必填）
  - 添加消费备注（可选）
  - 自动记录消费时间
- **删除消费记录**
  - 支持滑动删除
  - 支持长按删除
- **消费类别管理**
  - 预设类别：餐饮、购物、交通、娱乐、医疗、教育、居住、其他
  - 每类别对应不同图标和颜色

### 3.2 消费统计
- **按时间维度统计**
  - 每日消费统计
  - 每周消费统计
  - 每月消费统计
- **图表展示**
  - 柱状图：展示日/周/月的消费金额趋势
  - 饼图：展示各类别的消费占比
- **时间筛选**
  - 支持切换不同日期/周/月查看

### 3.3 记录列表
- 按时间倒序显示所有消费记录
- 显示消费金额、类别、备注、时间
- 支持按类别筛选

## 4. UI/UX 设计方向

### 4.1 整体视觉风格
- Material Design 3 设计语言
- 简洁清新的界面风格
- 圆润的卡片式布局

### 4.2 颜色方案
- **主色**: 蓝色 (#2196F3)
- **强调色**: 橙色 (#FF9800)
- **背景色**: 浅灰色 (#F5F5F5)
- **卡片色**: 白色 (#FFFFFF)
- **文字色**: 深灰色 (#212121)

### 4.3 布局方式
- **底部导航栏**：首页（记录列表）、统计、关于
- **首页**：消费记录列表 + 悬浮添加按钮
- **添加记录**：底部弹出对话框
- **统计页面**：时间选择器 + 柱状图 + 饼图

### 4.4 预设消费类别及颜色
| 类别 | 图标 | 颜色 |
|------|------|------|
| 餐饮 | restaurant | #FF5722 |
| 购物 | shopping_bag | #E91E63 |
| 交通 | directions_car | #9C27B0 |
| 娱乐 | movie | #673AB7 |
| 医疗 | local_hospital | #F44336 |
| 教育 | school | #3F51B5 |
| 居住 | home | #009688 |
| 其他 | more_horiz | #607D8B |

## 5. 数据模型

### ExpenseRecord（消费记录）
- `id`: Long (主键，自增)
- `amount`: Double (消费金额)
- `category`: String (消费类别)
- `note`: String? (备注，可为空)
- `timestamp`: Long (记录时间戳)

## 6. 项目结构

```
app/
├── data/
│   ├── local/
│   │   ├── ExpenseDatabase.kt
│   │   ├── ExpenseDao.kt
│   │   └── entity/
│   │       └── ExpenseRecord.kt
│   └── repository/
│       └── ExpenseRepositoryImpl.kt
├── domain/
│   ├── model/
│   │   └── Expense.kt
│   ├── repository/
│   │   └── ExpenseRepository.kt
│   └── usecase/
│       ├── AddExpenseUseCase.kt
│       ├── DeleteExpenseUseCase.kt
│       └── GetExpenseStatsUseCase.kt
├── ui/
│   ├── home/
│   │   ├── HomeFragment.kt
│   │   ├── HomeViewModel.kt
│   │   └── ExpenseAdapter.kt
│   ├── add/
│   │   └── AddExpenseDialog.kt
│   ├── statistics/
│   │   ├── StatisticsFragment.kt
│   │   └── StatisticsViewModel.kt
│   └── MainActivity.kt
└── di/
    └── AppModule.kt
```
