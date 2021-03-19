package br.com.pedrotlf.desafioshippmobile.ui.establishment

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresPermission
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.pedrotlf.desafioshippmobile.BuildConfig
import br.com.pedrotlf.desafioshippmobile.EstablishmentOrder
import br.com.pedrotlf.desafioshippmobile.R
import br.com.pedrotlf.desafioshippmobile.databinding.FragmentEstablishmentsListBinding
import br.com.pedrotlf.desafioshippmobile.establishments.EstablishmentsViewModel
import br.com.pedrotlf.desafioshippmobile.establishments.PlacesAdapter
import br.com.pedrotlf.desafioshippmobile.utils.BaseActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.toast

@AndroidEntryPoint
class NEWEstablishmentListFragment: Fragment(R.layout.fragment_establishments_list) {
    private val viewModel: EstablishmentsViewModel by viewModels()

    private lateinit var placesClient: PlacesClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentEstablishmentsListBinding.bind(view)

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
        val progress =
            indeterminateProgressDialog(R.string.search_establishments_current_location_loading)
        progress.setCancelable(false)
        viewModel.updateCurrentLocation(placesClient) { updated -> progress.dismiss() }
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

    private val establishmentClicked: (EstablishmentOrder) -> Unit = { establishmentOrder ->
        if (establishmentOrder.photo == null || establishmentOrder.latLng == null) {
            val progress =
                indeterminateProgressDialog(getString(R.string.search_establishments_get_details_loading))
            progress.setCancelable(false)
            viewModel.getPlaceDetails(placesClient, establishmentOrder.id) { latLng, photoMetadata ->
                if (latLng != null) {
                    if (photoMetadata != null) {
                        viewModel.getPlacePhoto(placesClient, photoMetadata) { returnedPhoto ->
                            progress.dismiss()
                            establishmentOrder.photo = returnedPhoto
//                            establishmentClicked(establishmentOrder)
                        }
                    } else {
                        progress.dismiss()
//                        establishmentClicked(establishmentOrder)
                    }
                } else {
                    progress.dismiss()
                    toast(R.string.search_establishments_get_details_error)
                }
            }
        } else {
//            establishmentClicked(establishmentOrder)
        }
    }

    private fun FragmentEstablishmentsListBinding.configureSearchView() {
        search.setOnQueryTextFocusChangeListener { _, hasFocus ->
            search.isSelected = hasFocus
        }
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search.clearFocus()
                progressBar.visibility = View.VISIBLE
                viewModel.getPlacesPredictions(placesClient, query){
                    progressBar.visibility = View.GONE
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrEmpty())
                    viewModel.getPlacesPredictions(placesClient, newText)
                return false
            }
        })
    }
}