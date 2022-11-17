package com.jacksafblaze.notes.di

import android.content.Context
import android.net.ConnectivityManager
import com.jacksafblaze.notes.util.NetworkStateManager
import com.jacksafblaze.notes.util.NetworkStateManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideNetworkStateManager(connectivityManager: ConnectivityManager): NetworkStateManager{
        return NetworkStateManagerImpl(connectivityManager)
    }
}