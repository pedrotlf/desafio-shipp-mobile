package br.com.pedrotlf.desafioshippmobile.data.order

import android.os.Parcelable
import br.com.pedrotlf.desafioshippmobile.data.Place
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
        val place: Place?,
        var orderDetails: String?,
        var price: Double?,
        var totalPrice: Double?,
        var clientLatLng: LatLng?
): Parcelable {
        constructor(place: Place?, clientLatLng: LatLng?): this(place, null, null, null, clientLatLng)
}