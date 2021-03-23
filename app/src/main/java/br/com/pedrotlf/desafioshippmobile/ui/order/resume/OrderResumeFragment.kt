package br.com.pedrotlf.desafioshippmobile.ui.order.resume

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.pedrotlf.desafioshippmobile.R
import br.com.pedrotlf.desafioshippmobile.databinding.FragmentOrderResumeBinding
import br.com.pedrotlf.desafioshippmobile.ui.order.OrderViewModel
import br.com.pedrotlf.desafioshippmobile.ui.order.price.OrderPriceFragmentDirections
import br.com.pedrotlf.desafioshippmobile.utils.DataState
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
import java.text.NumberFormat

@AndroidEntryPoint
class OrderResumeFragment : Fragment(R.layout.fragment_order_resume) {
    private val fragViewModel: OrderResumeViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()

    private var progress: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentOrderResumeBinding.bind(view)

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
            card.description.text = orderViewModel.orderDetails

            val totalPrice = try {
                NumberFormat.getCurrencyInstance().format(orderViewModel.orderTotalPrice)
            } catch (e: Exception){
                0
            }
            card.price.text = getString(R.string.establishment_order_resume_price_label, totalPrice)

            btnBack.setOnClickListener { requireActivity().onBackPressed() }
            btnNext.setOnClickListener {
                orderViewModel.order?.let { fragViewModel.onNextClicked(it) }
            }
            paymentCard.setOnClickListener {
                val action = OrderResumeFragmentDirections.actionOrderResumeFragmentToCardsFragment()
                findNavController().navigate(action)
            }
        }

        subscribeObservers()
    }

    private fun subscribeObservers(){
        fragViewModel.checkoutState.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    progress?.dismiss()
                    val action = OrderResumeFragmentDirections.actionOrderResumeFragmentToOrderSuccessFragment(dataState.data)
                    findNavController().navigate(action)
                }
                is DataState.Error -> {
                    toast(R.string.unknown_error)
                    progress?.dismiss()
                }

                DataState.Loading -> {
                    progress = indeterminateProgressDialog(R.string.establishment_order_resume_loading)
                    progress?.setCancelable(false)
                }
            }.exhaustive
        })
    }
}