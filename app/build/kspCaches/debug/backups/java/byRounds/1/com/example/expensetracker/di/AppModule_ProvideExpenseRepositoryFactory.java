package com.example.expensetracker.di;

import com.example.expensetracker.data.local.ExpenseDao;
import com.example.expensetracker.domain.repository.ExpenseRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideExpenseRepositoryFactory implements Factory<ExpenseRepository> {
  private final Provider<ExpenseDao> expenseDaoProvider;

  public AppModule_ProvideExpenseRepositoryFactory(Provider<ExpenseDao> expenseDaoProvider) {
    this.expenseDaoProvider = expenseDaoProvider;
  }

  @Override
  public ExpenseRepository get() {
    return provideExpenseRepository(expenseDaoProvider.get());
  }

  public static AppModule_ProvideExpenseRepositoryFactory create(
      Provider<ExpenseDao> expenseDaoProvider) {
    return new AppModule_ProvideExpenseRepositoryFactory(expenseDaoProvider);
  }

  public static ExpenseRepository provideExpenseRepository(ExpenseDao expenseDao) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideExpenseRepository(expenseDao));
  }
}
