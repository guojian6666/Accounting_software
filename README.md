# 记账助手 (ExpenseTracker)

一款简洁易用的安卓记账应用，支持消费记录的添加、删除、分类管理，以及按日/周/月统计消费情况，并提供直观的图表展示。

## 项目结构

```
ExpenseTracker/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/expensetracker/
│   │   │   ├── data/                    # 数据层
│   │   │   │   ├── local/               # 本地数据库
│   │   │   │   │   ├── ExpenseDatabase.kt
│   │   │   │   │   ├── ExpenseDao.kt
│   │   │   │   │   └── entity/
│   │   │   │   │       └── ExpenseRecord.kt
│   │   │   │   └── repository/
│   │   │   │       └── ExpenseRepositoryImpl.kt
│   │   │   ├── di/                      # 依赖注入
│   │   │   │   └── AppModule.kt
│   │   │   ├── domain/                  # 领域层
│   │   │   │   ├── model/
│   │   │   │   │   ├── Expense.kt
│   │   │   │   │   ├── ExpenseCategory.kt
│   │   │   │   │   └── StatisticsPeriod.kt
│   │   │   │   ├── repository/
│   │   │   │   │   └── ExpenseRepository.kt
│   │   │   │   └── usecase/
│   │   │   │       ├── AddExpenseUseCase.kt
│   │   │   │       ├── DeleteExpenseUseCase.kt
│   │   │   │       └── GetExpenseStatsUseCase.kt
│   │   │   ├── ui/                      # UI层
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── about/
│   │   │   │   │   └── AboutFragment.kt
│   │   │   │   ├── home/
│   │   │   │   │   ├── HomeFragment.kt
│   │   │   │   │   ├── HomeViewModel.kt
│   │   │   │   │   └── ExpenseAdapter.kt
│   │   │   │   └── statistics/
│   │   │   │       ├── StatisticsFragment.kt
│   │   │   │       └── StatisticsViewModel.kt
│   │   │   └── ExpenseTrackerApp.kt
│   │   ├── res/                         # 资源文件
│   │   │   ├── layout/
│   │   │   ├── values/
│   │   │   ├── menu/
│   │   │   ├── color/
│   │   │   └── mipmap-anydpi-v26/
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle                         # 项目级构建配置
├── settings.gradle
├── gradle.properties
└── gradle/wrapper/
    ├── gradle-wrapper.properties
    ├── gradlew
    └── gradlew.bat
```

## 技术栈

- **语言**: Kotlin
- **最低SDK**: 24 (Android 7.0)
- **目标SDK**: 34 (Android 14)
- **架构**: MVVM + Clean Architecture
- **依赖注入**: Hilt
- **数据库**: Room
- **异步处理**: Kotlin Coroutines + Flow
- **图表库**: MPAndroidChart
- **UI框架**: Material Design 3

## 功能特性

### 消费记录管理
- 添加消费记录（金额、类别、备注）
- 删除消费记录
- 8种预设消费类别（餐饮、购物、交通、娱乐、医疗、教育、居住、其他）

### 消费统计
- 按日/周/月维度统计
- 柱状图展示消费趋势
- 饼图展示类别占比
- 时间导航功能

## 在 Android Studio 中构建

### 步骤 1: 导入项目

1. 打开 Android Studio
2. 选择 "Open an Existing Project"
3. 选择项目根目录

### 步骤 2: 配置 Gradle

如果首次打开，需要配置 Gradle Wrapper：
1. 打开 File → Settings → Build, Execution, Deployment → Gradle
2. 确保 Gradle JDK 配置正确（建议使用 JDK 17）
3. 点击 "OK"

### 步骤 3: 同步项目

Android Studio 会自动提示同步项目，点击 "Sync Now" 等待同步完成。

### 步骤 4: 构建 APK

1. 选择 Build → Make Project 或按 Ctrl+F9
2. 构建完成后，选择 Build → Build Bundle(s) / APK(s) → Build APK(s)
3. APK 文件将生成在 `app/build/outputs/apk/debug/` 目录

### 步骤 5: 安装和运行

1. 连接安卓设备或启动模拟器
2. 点击 Run → Run 'app' 或按 Shift+F10

## 消费类别说明

| 类别 | 颜色 | 说明 |
|------|------|------|
| 餐饮 | 橙色 | 食物、餐饮消费 |
| 购物 | 粉色 | 购物、零售消费 |
| 交通 | 紫色 | 出行、交通费用 |
| 娱乐 | 深紫 | 娱乐、休闲活动 |
| 医疗 | 红色 | 医疗、健康支出 |
| 教育 | 蓝色 | 教育、学习费用 |
| 居住 | 青色 | 住房、水电物业 |
| 其他 | 灰色 | 其他杂项支出 |

## 数据存储

应用使用 Room 数据库本地存储所有消费记录，数据保存在设备内部存储中。

## 版本信息

- 当前版本: 1.0
- 发布日期: 2026-05-16
