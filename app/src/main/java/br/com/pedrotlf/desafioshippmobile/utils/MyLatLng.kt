package br.com.pedrotlf.desafioshippmobile.utils

import com.google.android.gms.maps.model.LatLng

data class MyLatLng(
    val latitude: Double,
    val longitude: Double
){
    constructor(latLng: LatLng): this(latLng.latitude, latLng.longitude)
}