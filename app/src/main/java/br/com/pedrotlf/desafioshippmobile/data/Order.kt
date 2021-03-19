package br.com.pedrotlf.desafioshippmobile.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
        val place: Place,
        var orderDetails: String?,
        var price: Double?,
        var totalPrice: Double?
): Parcelable {
        constructor(place: Place): this(place, null, null, null)
}