package br.com.pedrotlf.desafioshippmobile.ui.order.description

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.pedrotlf.desafioshippmobile.data.Place
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class OrderDescriptionViewModel @AssistedInject constructor(
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val place = state.get<Place>("place")

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
}