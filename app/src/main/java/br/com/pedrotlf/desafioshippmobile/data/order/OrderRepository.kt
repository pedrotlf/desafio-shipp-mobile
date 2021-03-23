package br.com.pedrotlf.desafioshippmobile.data.order

import br.com.pedrotlf.desafioshippmobile.api.OrderApi
import br.com.pedrotlf.desafioshippmobile.api.OrderRequest
import br.com.pedrotlf.desafioshippmobile.data.card.Card
import br.com.pedrotlf.desafioshippmobile.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OrderRepository constructor(private val orderApi: OrderApi) {

    suspend fun getResumeValidation(order: Order): Flow<DataState<Order>> = flow {
        val orderPlaceLatLng = order.place?.latLng
        val clientLatLng = order.clientLatLng
        val request = OrderRequest(orderPlaceLatLng?.latitude, orderPlaceLatLng?.longitude, clientLatLng?.latitude, clientLatLng?.longitude, order.price)

        emit(DataState.Loading)
        try {
            val response = orderApi.validateResume(request)
            emit(DataState.Success(order.apply { totalPrice = response.totalValue }))
        }catch (e: Exception){
            emit(DataState.Error(e))
        }
    }

    suspend fun getCheckout(order: Order, card: Card): Flow<DataState<Order>> = flow {
        val orderPlaceLatLng = order.place?.latLng
        val clientLatLng = order.clientLatLng
        val request = OrderRequest(
            orderPlaceLatLng?.latitude,
            orderPlaceLatLng?.longitude,
            clientLatLng?.latitude,
            clientLatLng?.longitude,
            order.price,
            card.number,
            card.cvv,
            card.expirationDate
        )

        emit(DataState.Loading)
        try {
            val response = orderApi.checkout(request)
            emit(DataState.Success(order))
        }catch (e: Exception){
            emit(DataState.Error(e))
        }
    }
}