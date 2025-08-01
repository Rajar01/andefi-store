package store.andefi.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import store.andefi.data.local.dao.AccountDao
import store.andefi.data.remote.api.AccountApi
import store.andefi.service.AccountService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideAccountService(accountDao: AccountDao, accountApi: AccountApi): AccountService {
        return AccountService(accountDao, accountApi)
    }
}