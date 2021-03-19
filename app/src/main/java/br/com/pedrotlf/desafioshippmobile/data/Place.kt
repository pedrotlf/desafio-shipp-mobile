package br.com.pedrotlf.desafioshippmobile.data

import android.graphics.Bitmap
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Place(
    val id: String,
    var name: String?,
    var address: String?,
    var city: String?,
    var photo: Bitmap?,
    var latLng: LatLng?,
): Parcelable