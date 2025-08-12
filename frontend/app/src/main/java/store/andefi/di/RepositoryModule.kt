package store.andefi.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import store.andefi.data.local.dao.AccountDao
import store.andefi.data.remote.api.AccountApi
import store.andefi.data.remote.api.CartApi
import store.andefi.data.remote.api.OrderApi
import store.andefi.data.remote.api.ProductApi
import store.andefi.data.remote.api.ShippingAddressApi
import store.andefi.data.repository.AccountRepository
import store.andefi.data.repository.CartRepository
import store.andefi.data.repository.OrderRepository
import store.andefi.data.repository.ProductRepository
import store.andefi.data.repository.ShippingAddressRepository
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

    @Provides
    @Singleton
    fun provideOrderRepository(orderApi: OrderApi): OrderRepository {
        return OrderRepository(orderApi)
    }

    @Provides
    @Singleton
    fun provideShippingAddressRepository(shippingAddressApi: ShippingAddressApi): ShippingAddressRepository {
        return ShippingAddressRepository(shippingAddressApi)
    }
}