package br.com.pedrotlf.desafioshippmobile.estabelecimentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.pedrotlf.desafioshippmobile.BaseFragment
import br.com.pedrotlf.desafioshippmobile.R

class EstablishmentOrderDescriptionFragment: BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setView(R.layout.fragment_establishment_order_description)
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}