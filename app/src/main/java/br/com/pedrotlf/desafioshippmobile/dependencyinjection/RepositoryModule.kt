package br.com.pedrotlf.desafioshippmobile.dependencyinjection

import br.com.pedrotlf.desafioshippmobile.api.OrderApi
import br.com.pedrotlf.desafioshippmobile.data.order.OrderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideOrderRepository(orderApi: OrderApi): OrderRepository {
        return OrderRepository(orderApi)
    }
}