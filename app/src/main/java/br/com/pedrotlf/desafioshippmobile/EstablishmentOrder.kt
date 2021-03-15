package br.com.pedrotlf.desafioshippmobile

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class EstablishmentOrder(
        val id: String,
        var name: String?,
        var address: String?,
        var city: String?,
        var photo: Bitmap?,
        var latLng: LatLng?,
        var orderDetails: String?,
        var price: Double?,
        var totalPrice: Double?
): Parcelable{
        constructor(parcel: Parcel) : this(
                parcel.readString() ?: "",
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readParcelable(Bitmap::class.java.classLoader),
                parcel.readParcelable(LatLng::class.java.classLoader),
                parcel.readString(),
                parcel.readValue(Double::class.java.classLoader) as? Double,
                parcel.readValue(Double::class.java.classLoader) as? Double
        )

        constructor(id: String, name: String?, address: String?, city: String?, photo: Bitmap?, latLng: LatLng?): this(id, name, address, city, photo, latLng, null, null, null)

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(id)
                parcel.writeString(name)
                parcel.writeString(address)
                parcel.writeString(city)
                parcel.writeParcelable(photo, flags)
                parcel.writeParcelable(latLng, flags)
                parcel.writeString(orderDetails)
                parcel.writeValue(price)
                parcel.writeValue(totalPrice)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<EstablishmentOrder> {
                override fun createFromParcel(parcel: Parcel): EstablishmentOrder {
                        return EstablishmentOrder(parcel)
                }

                override fun newArray(size: Int): Array<EstablishmentOrder?> {
                        return arrayOfNulls(size)
                }
        }
}