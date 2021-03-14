package br.com.pedrotlf.desafioshippmobile.estabelecimentos

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import br.com.pedrotlf.desafioshippmobile.BuildConfig
import br.com.pedrotlf.desafioshippmobile.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.*
import com.google.android.libraries.places.api.net.*
import java.lang.IllegalArgumentException

class EstablishmentsViewModel(application: Application) : AndroidViewModel(application) {
    private var placesClient: PlacesClient? = null
    private var autocompleteSessionToken: AutocompleteSessionToken? = null

    private var currentlyLocation: RectangularBounds? = null

    fun setPlacesClient(context: Context){
        Places.initialize(context, BuildConfig.PLACES_KEY)
        placesClient = Places.createClient(context)
        autocompleteSessionToken = AutocompleteSessionToken.newInstance()
    }

    @RequiresPermission(allOf = ["android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_WIFI_STATE"])
    fun updateCurrentlyLocation(context: Context){
        val placeFields: List<Place.Field> = listOf(Place.Field.LAT_LNG)
        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

        placesClient?.findCurrentPlace(request)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val response = task.result
                val closestResult = response?.placeLikelihoods?.get(0)?.place?.latLng
                val impreciseResult = response?.placeLikelihoods?.get(1)?.place?.latLng
                currentlyLocation = try {
                    closestResult?.let { RectangularBounds.newInstance(it, impreciseResult ?: it) }
                }catch (e: IllegalArgumentException){
                    closestResult?.let { RectangularBounds.newInstance(it, it) }
                }
            } else {
                val exception = task.exception
                if (exception is ApiException) {
                    exception.printStackTrace()
                    updateCurrentlyLocation(context)
                }
            }
        }
    }

    fun getPlaceDetails(placeId: String, callback: (String?, String?, PhotoMetadata?)->Unit){
        if(placesClient != null) {
            placesClient?.fetchPlace(
                FetchPlaceRequest.newInstance(
                    placeId,
                    listOf(Place.Field.ADDRESS, Place.Field.NAME, Place.Field.PHOTO_METADATAS)
                )
            )?.addOnSuccessListener { response ->
                val address = response.place.address
                val name = response.place.name
                val photoMetadata = if (response.place.photoMetadatas.isNullOrEmpty()) null else response.place.photoMetadatas?.get(0)
                if (!address.isNullOrBlank() && !name.isNullOrBlank()) {
                    callback(name, address, photoMetadata)
                }else{
                    callback(null, null, null)
                }
            }
        }else{
            val exception = Exception(getApplication<Application>().getString(R.string.places_client_not_set))
            exception.printStackTrace()
            callback(null, null, null)
        }
    }

    fun getPlacePhoto(metadata: PhotoMetadata, callback: (Bitmap?) -> Unit){
        val request = FetchPhotoRequest.builder(metadata).build()
        placesClient?.fetchPhoto(request)
            ?.addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->
                val bitmap = fetchPhotoResponse.bitmap
                callback(bitmap)
            }?.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    exception.printStackTrace()
                    callback(null)
                }
            }
    }

    fun getPlacesPredictions(query: String, callback: (List<AutocompletePrediction>?) -> Unit){
        val request = FindAutocompletePredictionsRequest.builder()
            .setTypeFilter(TypeFilter.ESTABLISHMENT)
            .setLocationBias(currentlyLocation)
            .setSessionToken(autocompleteSessionToken)
            .setQuery(query)
            .build()
        placesClient?.findAutocompletePredictions(request)
            ?.addOnSuccessListener { response ->
                callback(response.autocompletePredictions)
            }
            ?.addOnFailureListener {
                it.printStackTrace()
                callback(null)
            }
            ?.addOnCanceledListener {
                callback(null)
            }
    }
}