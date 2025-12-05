package com.payable.app.di

import android.content.Context
import androidx.room.Room
import com.payable.app.data.local.AppDatabase
import com.payable.app.data.local.ExpenseDao
import com.payable.app.data.local.OnboardingDataStore
import com.payable.app.data.local.UserFinanceDao
import com.payable.app.data.repository.ExpenseRepository
import com.payable.app.data.repository.ExpenseRepositoryImpl
import com.payable.app.data.repository.UserFinanceRepository
import com.payable.app.data.repository.UserFinanceRepositoryImpl
import com.payable.app.domain.usecase.CalculateAffordabilityUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOnboardingDataStore(
        @ApplicationContext context: Context,
    ): OnboardingDataStore = OnboardingDataStore(context)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "payable_db",
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideUserFinanceDao(
        db: AppDatabase,
    ): UserFinanceDao = db.userFinanceDao()

    @Provides
    fun provideExpenseDao(
        db: AppDatabase,
    ): ExpenseDao = db.expenseDao()

    @Provides
    @Singleton
    fun provideUserFinanceRepository(
        dao: UserFinanceDao,
    ): UserFinanceRepository = UserFinanceRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideExpenseRepository(
        dao: ExpenseDao,
    ): ExpenseRepository = ExpenseRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideCalculateAffordabilityUseCase(): CalculateAffordabilityUseCase =
        CalculateAffordabilityUseCase()
}

