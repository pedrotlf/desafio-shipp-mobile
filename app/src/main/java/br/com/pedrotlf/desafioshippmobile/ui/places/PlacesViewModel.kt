package br.com.pedrotlf.desafioshippmobile.ui.places

import android.graphics.Bitmap
import androidx.annotation.RequiresPermission
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import br.com.pedrotlf.desafioshippmobile.data.order.Order
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.*
import com.google.android.libraries.places.api.net.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import com.google.android.libraries.places.api.model.Place as GooglePlace

class PlacesViewModel @AssistedInject constructor(
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private var autocompleteSessionToken: AutocompleteSessionToken = AutocompleteSessionToken.newInstance()

    var currentLocation: LatLng? = null

    val searchQuery = state.getLiveData("searchQuery", "")

    private val placesEventChannel = Channel<PlacesEvent>()
    val placesEvent = placesEventChannel.receiveAsFlow()

    private var predictionList = MutableStateFlow<List<AutocompletePrediction>?>(listOf())
    private val predictionFlow = predictionList.flatMapLatest{
        predictionList
    }
    val autoCompletePredictions = predictionFlow.asLiveData()

    sealed class PlacesEvent{
        object CurrentLocationUpdated : PlacesEvent()
        object PlacesPredictionUpdated: PlacesEvent()
        data class NavigateToOrderDescription(val order: Order) : PlacesEvent()
    }

    fun onPlaceSelected(order: Order) = viewModelScope.launch {
        placesEventChannel.send(PlacesEvent.NavigateToOrderDescription(order))
    }

    @RequiresPermission(allOf = ["android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_WIFI_STATE"])
    fun updateCurrentLocation(placesClient: PlacesClient){
        if(currentLocation == null) {
            val placeFields: List<GooglePlace.Field> = listOf(GooglePlace.Field.LAT_LNG)
            val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

            placesClient.findCurrentPlace(request).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val response = task.result
                    val closestResult = response?.placeLikelihoods?.get(0)?.place?.latLng
                    currentLocation = closestResult
                    viewModelScope.launch {placesEventChannel.send(PlacesEvent.CurrentLocationUpdated)}
                } else {
                    val exception = task.exception
                    if (exception is ApiException) {
                        exception.printStackTrace()
                        updateCurrentLocation(placesClient)
                    } else {
                        viewModelScope.launch {placesEventChannel.send(PlacesEvent.CurrentLocationUpdated)}
                    }
                }
            }
        } else {
            viewModelScope.launch {placesEventChannel.send(PlacesEvent.CurrentLocationUpdated)}
        }
    }

    fun getPlaceDetails(placesClient: PlacesClient, placeId: String, callback: (LatLng?, PhotoMetadata?)->Unit){
        placesClient.fetchPlace(
            FetchPlaceRequest.newInstance(
                placeId,
                listOf(GooglePlace.Field.LAT_LNG, GooglePlace.Field.PHOTO_METADATAS)
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

    fun getPlacesPredictions(placesClient: PlacesClient){
        if(searchQuery.value.isNullOrBlank())
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
                .setQuery(searchQuery.value)
                .build()
            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    predictionList.value = response.autocompletePredictions
                    viewModelScope.launch {placesEventChannel.send(PlacesEvent.PlacesPredictionUpdated)}
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    predictionList.value = null
                    viewModelScope.launch {placesEventChannel.send(PlacesEvent.PlacesPredictionUpdated)}
                }
                .addOnCanceledListener {
                    viewModelScope.launch {placesEventChannel.send(PlacesEvent.PlacesPredictionUpdated)}
                }
        }
    }
}