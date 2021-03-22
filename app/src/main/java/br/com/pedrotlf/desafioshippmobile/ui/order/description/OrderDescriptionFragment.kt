package br.com.pedrotlf.desafioshippmobile.ui.order.description

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.pedrotlf.desafioshippmobile.R
import br.com.pedrotlf.desafioshippmobile.databinding.FragmentOrderDescriptionBinding
import br.com.pedrotlf.desafioshippmobile.ui.order.OrderViewModel
import br.com.pedrotlf.desafioshippmobile.utils.dipFromPixels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class OrderDescriptionFragment: Fragment(R.layout.fragment_order_description) {

    private val fragViewModel: OrderDescriptionViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentOrderDescriptionBinding.bind(view)

        binding.apply {
            card.placeTitle.text = orderViewModel.orderEstablishmentName
            card.address.text = orderViewModel.orderEstablishmentAddress
            if(orderViewModel.orderEstablishmentCity.isNotBlank())
                card.city.text = orderViewModel.orderEstablishmentCity
            else
                card.city.visibility = View.GONE
            Glide.with(requireContext().applicationContext)
                .load(orderViewModel.orderEstablishmentPhoto)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(requireContext().dipFromPixels(5f))))
                .into(card.image)

            btnBack.setOnClickListener { requireActivity().onBackPressed() }
            btnNext.setOnClickListener { fragViewModel.onNextClicked(orderViewModel.order) }

            orderDetails.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
                override fun afterTextChanged(s: Editable?) {
                    btnNext.isEnabled = !s.isNullOrBlank()
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    fragViewModel.orderDetails.value = s.toString()
                }
            })
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            fragViewModel.orderDescriptionEvents.collect{
                when(it){
                    is OrderDescriptionViewModel.OrderDescriptionEvents.NavigateToPrices -> {
                        val action = OrderDescriptionFragmentDirections.actionOrderDescriptionFragmentToOrderPriceFragment(it.order)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }
}