package br.com.pedrotlf.desafioshippmobile.data.order

import br.com.pedrotlf.desafioshippmobile.api.OrderApi
import br.com.pedrotlf.desafioshippmobile.api.OrderRequest
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

    suspend fun getCheckout(order: Order): Flow<DataState<Order>> = flow {
        val orderPlaceLatLng = order.place?.latLng
        val clientLatLng = order.clientLatLng
        val request = OrderRequest(orderPlaceLatLng?.latitude, orderPlaceLatLng?.longitude, clientLatLng?.latitude, clientLatLng?.longitude, order.price, "1111111111111111", "111", "11/28")

        emit(DataState.Loading)
        try {
            val response = orderApi.checkout(request)
            emit(DataState.Success(order))
        }catch (e: Exception){
            emit(DataState.Error(e))
        }
    }
}