package br.com.pedrotlf.desafioshippmobile.ui.order

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.pedrotlf.desafioshippmobile.data.order.Order
import br.com.pedrotlf.desafioshippmobile.data.Place
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class OrderViewModel @AssistedInject constructor(
        @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val order = state.get<Order>("order")

    val place = state.get<Place>("place") ?: order?.place

    var orderEstablishmentName = state.get<String>("placeName") ?: place?.name ?: ""
        set(value) {
            field = value
            state.set("placeName", value)
        }

    var orderEstablishmentAddress = state.get<String>("placeAddress") ?: place?.address ?: ""
        set(value) {
            field = value
            state.set("placeAddress", value)
        }

    var orderEstablishmentCity = state.get<String>("placeCity") ?: place?.city ?: ""
        set(value) {
            field = value
            state.set("placeCity", value)
        }

    var orderEstablishmentPhoto = state.get<Bitmap>("placePhoto") ?: place?.photo
        set(value) {
            field = value
            state.set("placePhoto", value)
        }

    var orderDetails = state.get<String>("orderDetails") ?: order?.orderDetails ?: ""
        set(value) {
            field = value
            state.set("orderDetails", value)
        }

    var orderPrice = state.get<Double>("orderPrice") ?: order?.price ?: 0.0
        set(value) {
            field = value
            state.set("orderPrice", value)
        }

    var orderTotalPrice = state.get<Double>("orderTotalPrice") ?: order?.totalPrice ?: 0.0
        set(value) {
            field = value
            state.set("orderTotalPrice", value)
        }


}