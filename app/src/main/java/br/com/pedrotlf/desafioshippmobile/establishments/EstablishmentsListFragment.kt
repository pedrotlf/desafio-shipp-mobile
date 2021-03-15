package br.com.pedrotlf.desafioshippmobile.establishments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.pedrotlf.desafioshippmobile.BaseActivity
import br.com.pedrotlf.desafioshippmobile.BaseFragment
import br.com.pedrotlf.desafioshippmobile.R
import kotlinx.android.synthetic.main.fragment_establishments_list.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.toast

class EstablishmentsListFragment: BaseFragment() {
    private var adapter: PlacesAdapter? = null

    private var establishmentClicked: (EstablishmentOrder) -> Unit = {}

    companion object{
        fun getInstance(establishmentClicked: (EstablishmentOrder) -> Unit): EstablishmentsListFragment{
            val frag = EstablishmentsListFragment()
            frag.establishmentClicked = establishmentClicked
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setView(R.layout.fragment_establishments_list)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        establishmentsViewModel.setPlacesClient(act)
        if((act as BaseActivity).checkLocationPermission())
            establishmentsViewModel.updateCurrentlyLocation(act)

        configureRecyclerView()
        configureSearchView()
    }

    private fun configureRecyclerView() {
        adapter = PlacesAdapter(act, establishmentsViewModel){ establishmentOrder ->
            if(establishmentOrder.photo == null) {
                val progress = indeterminateProgressDialog(getString(R.string.search_establishments_get_details_loading))
                progress.setCancelable(false)
                establishmentsViewModel.getPlaceDetails(establishmentOrder.id) { detailsName, detailsAddress, photoMetadata ->
                    if (!detailsName.isNullOrBlank() && !detailsAddress.isNullOrBlank()) {
                        if (photoMetadata != null) {
                            establishmentsViewModel.getPlacePhoto(photoMetadata) { returnedPhoto ->
                                progress.dismiss()
                                establishmentOrder.photo = returnedPhoto
                                establishmentClicked(establishmentOrder)
                            }
                        } else {
                            progress.dismiss()
                            establishmentClicked(establishmentOrder)
                        }
                    } else {
                        progress.dismiss()
                        toast(R.string.search_establishments_get_details_error)
                    }
                }
            } else {
                establishmentClicked(establishmentOrder)
            }
        }

        recyclerPlaces.layoutManager = LinearLayoutManager(act.applicationContext)
        recyclerPlaces.adapter = adapter
    }

    private fun configureSearchView() {
        search.setOnQueryTextFocusChangeListener { _, hasFocus ->
            search.isSelected = hasFocus
        }

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search.clearFocus()
                if (!query.isNullOrBlank()) {
                    recyclerPlaces.visibility = View.GONE
                    placesEmpty.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE

                    establishmentsViewModel.getPlacesPredictions(query) { predictionList ->
                        progressBar.visibility = View.GONE
                        if (!predictionList.isNullOrEmpty()) {
                            recyclerPlaces.visibility = View.VISIBLE
                            placesEmpty.visibility = View.GONE
                            adapter?.places = predictionList
                        } else {
                            recyclerPlaces.visibility = View.GONE
                            placesEmpty.visibility = View.VISIBLE
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    recyclerPlaces.visibility = View.GONE
                    placesEmpty.visibility = View.GONE
                }
                return false
            }

        })
    }
}