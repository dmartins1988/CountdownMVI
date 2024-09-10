package com.example.countdownmvi.di

import com.example.countdownmvi.domain.CountdownTimerRepository
import com.example.countdownmvi.domain.CountdownTimerRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCountdownTimerRepository(): CountdownTimerRepository {
        return CountdownTimerRepositoryImpl()
    }
}