package com.example.expensetracker.domain.usecase;

import com.example.expensetracker.domain.repository.ExpenseRepository;
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
public final class GetExpenseStatsUseCase_Factory implements Factory<GetExpenseStatsUseCase> {
  private final Provider<ExpenseRepository> repositoryProvider;

  public GetExpenseStatsUseCase_Factory(Provider<ExpenseRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetExpenseStatsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetExpenseStatsUseCase_Factory create(
      Provider<ExpenseRepository> repositoryProvider) {
    return new GetExpenseStatsUseCase_Factory(repositoryProvider);
  }

  public static GetExpenseStatsUseCase newInstance(ExpenseRepository repository) {
    return new GetExpenseStatsUseCase(repository);
  }
}
