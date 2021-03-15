package br.com.pedrotlf.desafioshippmobile

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng

data class EstablishmentOrder(
        val id: String,
        var name: String?,
        var address: String?,
        var city: String?,
        var photo: Bitmap?,
        var latLng: LatLng?,
        var orderDetails: String?,
        var price: String?,
        var totalPrice: Double?
){
    constructor(id: String, name: String?, address: String?, city: String?): this(id, name, address, city, null,null, null, null, null)
    constructor(id: String, name: String?, address: String?, city: String?, photo: Bitmap?, latLng: LatLng?): this(id, name, address, city, photo, latLng, null, null, null)
}