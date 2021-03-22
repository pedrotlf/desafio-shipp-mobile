package br.com.pedrotlf.desafioshippmobile.ui.order.price

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.pedrotlf.desafioshippmobile.R
import br.com.pedrotlf.desafioshippmobile.databinding.FragmentOrderPriceBinding
import br.com.pedrotlf.desafioshippmobile.ui.order.OrderViewModel
import br.com.pedrotlf.desafioshippmobile.utils.DataState
import br.com.pedrotlf.desafioshippmobile.utils.MoneyTextWatcher
import br.com.pedrotlf.desafioshippmobile.utils.dipFromPixels
import br.com.pedrotlf.desafioshippmobile.utils.exhaustive
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.toast

@AndroidEntryPoint
class OrderPriceFragment: Fragment(R.layout.fragment_order_price) {
    private val fragViewModel: OrderPriceViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()

    private var progress: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentOrderPriceBinding.bind(view)

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
            card.description.visibility = View.VISIBLE
            card.description.text = orderViewModel.orderDetails

            inputPrice.addTextChangedListener(MoneyTextWatcher(inputPrice) { priceValue, priceString ->
                btnNext.isEnabled = (priceValue > 0)
                fragViewModel.orderPrice.value = priceString
            })

            btnBack.setOnClickListener { requireActivity().onBackPressed() }

            btnNext.setOnClickListener {
                orderViewModel.order?.let { fragViewModel.onNextClicked(it) }
            }
        }

        subscribeObservers()
    }

    private fun subscribeObservers(){
        fragViewModel.validationState.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    progress?.dismiss()
                    val action = OrderPriceFragmentDirections.actionOrderPriceFragmentToOrderResumeFragment(dataState.data)
                    findNavController().navigate(action)
                }
                is DataState.Error -> {
                    toast(R.string.unknown_error)
                    progress?.dismiss()
                }

                DataState.Loading -> {
                    progress = indeterminateProgressDialog(R.string.establishment_order_price_confirm_loading)
                    progress?.setCancelable(false)
                }
            }.exhaustive
        })
    }
}