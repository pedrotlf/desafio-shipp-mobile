package br.com.pedrotlf.desafioshippmobile.establishments

import android.graphics.Bitmap

data class EstablishmentOrder(
        val id: String,
        var name: String?,
        var address: String?,
        var city: String?,
        var photo: Bitmap?,
        var orderDetails: String?,
        var price: Float?
){
    constructor(id: String, name: String?, address: String?, city: String?): this(id, name, address, city, null, null, null)
    constructor(id: String, name: String?, address: String?, city: String?, photo: Bitmap?): this(id, name, address, city, photo, null, null)
}