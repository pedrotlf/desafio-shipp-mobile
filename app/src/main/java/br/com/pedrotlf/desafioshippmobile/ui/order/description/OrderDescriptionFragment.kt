package br.com.pedrotlf.desafioshippmobile.ui.order.description

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.com.pedrotlf.desafioshippmobile.R
import br.com.pedrotlf.desafioshippmobile.databinding.FragmentOrderDescriptionBinding
import br.com.pedrotlf.desafioshippmobile.utils.dipFromPixels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDescriptionFragment: Fragment(R.layout.fragment_order_description) {

    private val viewModel: OrderDescriptionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentOrderDescriptionBinding.bind(view)

        binding.apply {
            card.placeTitle.text = viewModel.orderEstablishmentName
            card.address.text = viewModel.orderEstablishmentAddress
            card.city.text = viewModel.orderEstablishmentCity
            Glide.with(requireContext().applicationContext)
                .load(viewModel.orderEstablishmentPhoto)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(requireContext().dipFromPixels(5f))))
                .into(card.image)
        }
    }
}