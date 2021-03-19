package br.com.pedrotlf.desafioshippmobile.establishments

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import br.com.pedrotlf.desafioshippmobile.BuildConfig
import br.com.pedrotlf.desafioshippmobile.R
import br.com.pedrotlf.desafioshippmobile.utils.MyLatLng
import br.com.pedrotlf.desafioshippmobile.utils.Prefs
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.*
import com.google.android.libraries.places.api.net.*
import java.lang.IllegalArgumentException


class EstablishmentsViewModel : ViewModel() {
//    private val prefs = Prefs(application)

    private var currentLocation: MyLatLng? = null
    private var autocompleteSessionToken: AutocompleteSessionToken = AutocompleteSessionToken.newInstance()

    @RequiresPermission(allOf = ["android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_WIFI_STATE"])
    fun updateCurrentLocation(placesClient: PlacesClient, callback: (Boolean)->Unit){
        val placeFields: List<Place.Field> = listOf(Place.Field.LAT_LNG)
        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

        placesClient.findCurrentPlace(request).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val response = task.result
                val closestResult = response?.placeLikelihoods?.get(0)?.place?.latLng
                if(closestResult != null) {
                    currentLocation = MyLatLng(closestResult)
                    callback(true)
                } else
                    callback(false)
            } else {
                val exception = task.exception
                if (exception is ApiException) {
                    exception.printStackTrace()
                    updateCurrentLocation(placesClient, callback)
                }
                callback(false)
            }
        }
    }

    fun getPlaceDetails(placesClient: PlacesClient, placeId: String, callback: (LatLng?, PhotoMetadata?)->Unit){
        placesClient.fetchPlace(
            FetchPlaceRequest.newInstance(
                placeId,
                listOf(Place.Field.LAT_LNG, Place.Field.PHOTO_METADATAS)
            )
        ).addOnSuccessListener { response ->
            val latLng = response.place.latLng
            val photoMetadata = if (response.place.photoMetadatas.isNullOrEmpty()) null else response.place.photoMetadatas?.get(0)
            if (latLng != null) {
                callback(latLng, photoMetadata)
            }else{
                callback(null, null)
            }
        }
    }

    fun getPlacePhoto(placesClient: PlacesClient, metadata: PhotoMetadata, callback: (Bitmap?) -> Unit){
        val request = FetchPhotoRequest.builder(metadata).build()
        placesClient.fetchPhoto(request)
            .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->
                val bitmap = fetchPhotoResponse.bitmap
                callback(bitmap)
            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    exception.printStackTrace()
                    callback(null)
                }
            }
    }

    fun getPlacesPredictions(placesClient: PlacesClient, query: String, callback: (List<AutocompletePrediction>?) -> Unit){
        val bounds = currentLocation?.let {
            RectangularBounds.newInstance(LatLng(it.latitude, it.longitude), LatLng(it.latitude, it.longitude))
        }
        val request = FindAutocompletePredictionsRequest.builder()
            .setTypeFilter(TypeFilter.ESTABLISHMENT)
            .setLocationBias(bounds)
            .setSessionToken(autocompleteSessionToken)
            .setQuery(query)
            .build()
        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                callback(response.autocompletePredictions)
            }
            .addOnFailureListener {
                it.printStackTrace()
                callback(null)
            }
            .addOnCanceledListener {
                callback(null)
            }
    }
}