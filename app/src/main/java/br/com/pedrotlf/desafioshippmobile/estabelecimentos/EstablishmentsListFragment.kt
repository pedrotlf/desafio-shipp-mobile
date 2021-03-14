package br.com.pedrotlf.desafioshippmobile.estabelecimentos

import android.os.Bundle
import android.util.Log
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

class EstablishmentsListFragment: BaseFragment() {
    private var adapter: PlacesAdapter? = null

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
        adapter = PlacesAdapter(act, establishmentsViewModel){ placeClicked ->
            establishmentsViewModel.getPlaceDetails(placeClicked.placeId) { name, address, photoMetadata ->
                if (!name.isNullOrBlank() && !address.isNullOrBlank()) {
                    Log.i("AHA", "Temos o endereço -> \"$address\" e o nome -> \"$name\"")
                } else {
                    //todo erro
                }
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