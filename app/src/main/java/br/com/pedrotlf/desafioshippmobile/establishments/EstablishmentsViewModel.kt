package br.com.pedrotlf.desafioshippmobile.establishments

import android.graphics.Bitmap
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import br.com.pedrotlf.desafioshippmobile.utils.MyLatLng
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.*
import com.google.android.libraries.places.api.net.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest


class EstablishmentsViewModel : ViewModel() {

    private var autocompleteSessionToken: AutocompleteSessionToken = AutocompleteSessionToken.newInstance()

    private var currentLocation: MyLatLng? = null

    private var predictionList = MutableStateFlow<List<AutocompletePrediction>?>(listOf())
    private val predictionFlow = predictionList.flatMapLatest{
        predictionList
    }
    val autoCompletePredictions = predictionFlow.asLiveData()

    @RequiresPermission(allOf = ["android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_WIFI_STATE"])
    fun updateCurrentLocation(placesClient: PlacesClient, callback: (Boolean)->Unit){
        if(currentLocation == null) {
            val placeFields: List<Place.Field> = listOf(Place.Field.LAT_LNG)
            val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

            placesClient.findCurrentPlace(request).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val response = task.result
                    val closestResult = response?.placeLikelihoods?.get(0)?.place?.latLng
                    if (closestResult != null) {
                        currentLocation = MyLatLng(closestResult)
                        callback(true)
                    } else
                        callback(false)
                } else {
                    val exception = task.exception
                    if (exception is ApiException) {
                        exception.printStackTrace()
                        updateCurrentLocation(placesClient, callback)
                    } else {
                        callback(false)
                    }
                }
            }
        } else {
            callback(true)
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

    fun getPlacesPredictions(placesClient: PlacesClient, query: String?, callback: ()->Unit = {}){
        if(query.isNullOrBlank())
            predictionList.value = listOf()
        else {
            val bounds = currentLocation?.let {
                RectangularBounds.newInstance(
                    LatLng(it.latitude, it.longitude),
                    LatLng(it.latitude, it.longitude)
                )
            }
            val request = FindAutocompletePredictionsRequest.builder()
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setLocationBias(bounds)
                .setSessionToken(autocompleteSessionToken)
                .setQuery(query)
                .build()
            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    predictionList.value = response.autocompletePredictions
                    callback()
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    predictionList.value = null
                    callback()
                }
                .addOnCanceledListener {
                    callback()
                }
        }
    }
}