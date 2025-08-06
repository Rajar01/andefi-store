package store.andefi.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import store.andefi.data.local.dao.AccountDao
import store.andefi.data.remote.api.AccountApi
import store.andefi.data.remote.api.ProductApi
import store.andefi.data.repository.AccountRepository
import store.andefi.data.repository.ProductRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAccountService(accountDao: AccountDao, accountApi: AccountApi): AccountRepository {
        return AccountRepository(accountDao, accountApi)
    }

    @Provides
    @Singleton
    fun provideProductService(productApi: ProductApi): ProductRepository {
        return ProductRepository(productApi)
    }
}