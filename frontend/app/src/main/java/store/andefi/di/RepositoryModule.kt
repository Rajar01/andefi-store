package store.andefi.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import store.andefi.data.local.dao.AccountDao
import store.andefi.data.remote.api.AccountApi
import store.andefi.data.remote.api.CartApi
import store.andefi.data.remote.api.ProductApi
import store.andefi.data.repository.AccountRepository
import store.andefi.data.repository.CartRepository
import store.andefi.data.repository.ProductRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAccountRepository(
        accountDao: AccountDao,
        accountApi: AccountApi
    ): AccountRepository {
        return AccountRepository(accountDao, accountApi)
    }

    @Provides
    @Singleton
    fun provideProductRepository(productApi: ProductApi): ProductRepository {
        return ProductRepository(productApi)
    }

    @Provides
    @Singleton
    fun provideCartRepository(cartApi: CartApi): CartRepository {
        return CartRepository(cartApi)
    }
}