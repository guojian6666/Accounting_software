package com.example.expensetracker.data.repository;

import com.example.expensetracker.data.local.ExpenseDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class ExpenseRepositoryImpl_Factory implements Factory<ExpenseRepositoryImpl> {
  private final Provider<ExpenseDao> expenseDaoProvider;

  public ExpenseRepositoryImpl_Factory(Provider<ExpenseDao> expenseDaoProvider) {
    this.expenseDaoProvider = expenseDaoProvider;
  }

  @Override
  public ExpenseRepositoryImpl get() {
    return newInstance(expenseDaoProvider.get());
  }

  public static ExpenseRepositoryImpl_Factory create(Provider<ExpenseDao> expenseDaoProvider) {
    return new ExpenseRepositoryImpl_Factory(expenseDaoProvider);
  }

  public static ExpenseRepositoryImpl newInstance(ExpenseDao expenseDao) {
    return new ExpenseRepositoryImpl(expenseDao);
  }
}
