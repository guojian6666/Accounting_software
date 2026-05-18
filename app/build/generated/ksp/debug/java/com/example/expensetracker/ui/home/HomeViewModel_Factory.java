package com.example.expensetracker.ui.home;

import com.example.expensetracker.domain.repository.ExpenseRepository;
import com.example.expensetracker.domain.usecase.AddExpenseUseCase;
import com.example.expensetracker.domain.usecase.DeleteExpenseUseCase;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<ExpenseRepository> repositoryProvider;

  private final Provider<AddExpenseUseCase> addExpenseUseCaseProvider;

  private final Provider<DeleteExpenseUseCase> deleteExpenseUseCaseProvider;

  public HomeViewModel_Factory(Provider<ExpenseRepository> repositoryProvider,
      Provider<AddExpenseUseCase> addExpenseUseCaseProvider,
      Provider<DeleteExpenseUseCase> deleteExpenseUseCaseProvider) {
    this.repositoryProvider = repositoryProvider;
    this.addExpenseUseCaseProvider = addExpenseUseCaseProvider;
    this.deleteExpenseUseCaseProvider = deleteExpenseUseCaseProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(repositoryProvider.get(), addExpenseUseCaseProvider.get(), deleteExpenseUseCaseProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<ExpenseRepository> repositoryProvider,
      Provider<AddExpenseUseCase> addExpenseUseCaseProvider,
      Provider<DeleteExpenseUseCase> deleteExpenseUseCaseProvider) {
    return new HomeViewModel_Factory(repositoryProvider, addExpenseUseCaseProvider, deleteExpenseUseCaseProvider);
  }

  public static HomeViewModel newInstance(ExpenseRepository repository,
      AddExpenseUseCase addExpenseUseCase, DeleteExpenseUseCase deleteExpenseUseCase) {
    return new HomeViewModel(repository, addExpenseUseCase, deleteExpenseUseCase);
  }
}
