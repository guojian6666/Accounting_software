package com.example.expensetracker.ui.statistics;

import com.example.expensetracker.domain.usecase.GetExpenseStatsUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class StatisticsViewModel_Factory implements Factory<StatisticsViewModel> {
  private final Provider<GetExpenseStatsUseCase> getExpenseStatsUseCaseProvider;

  public StatisticsViewModel_Factory(
      Provider<GetExpenseStatsUseCase> getExpenseStatsUseCaseProvider) {
    this.getExpenseStatsUseCaseProvider = getExpenseStatsUseCaseProvider;
  }

  @Override
  public StatisticsViewModel get() {
    return newInstance(getExpenseStatsUseCaseProvider.get());
  }

  public static StatisticsViewModel_Factory create(
      Provider<GetExpenseStatsUseCase> getExpenseStatsUseCaseProvider) {
    return new StatisticsViewModel_Factory(getExpenseStatsUseCaseProvider);
  }

  public static StatisticsViewModel newInstance(GetExpenseStatsUseCase getExpenseStatsUseCase) {
    return new StatisticsViewModel(getExpenseStatsUseCase);
  }
}
