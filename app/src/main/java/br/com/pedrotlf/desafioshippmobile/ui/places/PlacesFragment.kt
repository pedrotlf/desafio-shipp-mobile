package br.com.pedrotlf.desafioshippmobile.ui.places

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresPermission
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.pedrotlf.desafioshippmobile.BuildConfig
import br.com.pedrotlf.desafioshippmobile.R
import br.com.pedrotlf.desafioshippmobile.data.Order
import br.com.pedrotlf.desafioshippmobile.data.Place
import br.com.pedrotlf.desafioshippmobile.databinding.FragmentPlacesBinding
import br.com.pedrotlf.desafioshippmobile.utils.BaseActivity
import br.com.pedrotlf.desafioshippmobile.utils.exhaustive
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import org.jetbrains.anko.support.v4.indeterminateProgressDialog

@AndroidEntryPoint
class PlacesFragment: Fragment(R.layout.fragment_places) {
    private val viewModel: PlacesViewModel by viewModels()

    private lateinit var placesClient: PlacesClient

    private var progress: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentPlacesBinding.bind(view)

        val placesAdapter = PlacesAdapter(requireContext(), getPlaceDetail, establishmentClicked)

        binding.apply {
            recyclerPlaces.apply {
                adapter = placesAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            configureSearchView()

        }

        viewModel.autoCompletePredictions.observe(viewLifecycleOwner){
            placesAdapter.submitList(it){
                if (it == null){
                    binding.placesEmpty.visibility = View.VISIBLE
                } else {
                    binding.placesEmpty.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.placesEvent.collect {
                when(it){
                    is PlacesViewModel.PlacesEvent.CurrentLocationUpdated -> {
                        progress?.dismiss()
                    }
                    is PlacesViewModel.PlacesEvent.NavigateToOrderDescription -> {
                        val action = PlacesFragmentDirections.actionPlacesFragmentToOrderDescriptionFragment(it.order)
                        findNavController().navigate(action)
                    }
                    PlacesViewModel.PlacesEvent.PlacesPredictionUpdated -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }.exhaustive
            }
        }

        initializePlacesApi()
    }

    private fun initializePlacesApi() {
        Places.initialize(requireContext(), BuildConfig.PLACES_KEY)
        placesClient = Places.createClient(requireContext())

        if ((requireActivity() as BaseActivity).checkLocationPermission()) {
            updateUserLocation()
        } else {
            (requireActivity() as BaseActivity).requestLocationPermission {
                updateUserLocation()
            }
        }
    }

    @RequiresPermission(allOf = ["android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_WIFI_STATE"])
    private fun updateUserLocation() {
        progress =
            indeterminateProgressDialog(R.string.search_establishments_current_location_loading)
        progress?.show()
        progress?.setCancelable(false)
        viewModel.updateCurrentLocation(placesClient)
    }

    private val getPlaceDetail: (String, (LatLng?, Bitmap?)->Unit) -> Unit = { placeId, callback ->
        viewModel.getPlaceDetails(placesClient, placeId){ latLng, photoMetadata ->
            if(photoMetadata != null) {
                viewModel.getPlacePhoto(placesClient, photoMetadata) { photoBitmap ->
                    callback(latLng, photoBitmap)
                }
            } else {
                callback(latLng, null)
            }
        }
    }

    private val establishmentClicked: (Place) -> Unit = { place ->
        viewModel.onPlaceSelected(Order(place, viewModel.currentLocation))
    }

    private fun FragmentPlacesBinding.configureSearchView() {
        search.setOnQueryTextFocusChangeListener { _, hasFocus ->
            search.isSelected = hasFocus
        }
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search.clearFocus()
                progressBar.visibility = View.VISIBLE
                viewModel.getPlacesPredictions(placesClient)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchQuery.value = newText ?: ""
                if(newText.isNullOrEmpty())
                    viewModel.getPlacesPredictions(placesClient)
                return false
            }
        })
    }
}