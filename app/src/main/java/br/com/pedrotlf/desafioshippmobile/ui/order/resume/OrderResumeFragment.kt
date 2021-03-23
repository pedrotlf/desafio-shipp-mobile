package br.com.pedrotlf.desafioshippmobile.ui.order.resume

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.pedrotlf.desafioshippmobile.R
import br.com.pedrotlf.desafioshippmobile.databinding.FragmentOrderResumeBinding
import br.com.pedrotlf.desafioshippmobile.ui.order.OrderViewModel
import br.com.pedrotlf.desafioshippmobile.utils.DataState
import br.com.pedrotlf.desafioshippmobile.utils.dipFromPixels
import br.com.pedrotlf.desafioshippmobile.utils.exhaustive
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
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
                fragViewModel.onSelectCardClicked()
            }
        }

        registerEventCollector()

        subscribeObservers(binding)
        setOnResult()
    }

    private fun registerEventCollector() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            fragViewModel.orderResumeEvent.collect { event ->
                when (event) {
                    is OrderResumeViewModel.OrderResumeEvent.GoToCards -> {
                        val action =
                            OrderResumeFragmentDirections.actionOrderResumeFragmentToCardsFragment()
                        findNavController().navigate(action)
                    }
                    is OrderResumeViewModel.OrderResumeEvent.ShowCardSelectedMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is OrderResumeViewModel.OrderResumeEvent.GoToCheckout -> {
                        val action =
                            OrderResumeFragmentDirections.actionOrderResumeFragmentToOrderSuccessFragment(
                                event.order
                            )
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }
    }

    private fun subscribeObservers(binding: FragmentOrderResumeBinding){
        fragViewModel.checkoutState.observe(viewLifecycleOwner, { dataState ->
            @Suppress("IMPLICIT_CAST_TO_ANY")
            when (dataState) {
                is DataState.Success -> {
                    progress?.dismiss()
                    fragViewModel.goToCheckout(dataState.data)
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

        fragViewModel.selectedCard.observe(viewLifecycleOwner, {
            binding.paymentText.visibility = if(it == null) View.VISIBLE else View.INVISIBLE

            binding.cardSelectedLayout.isVisible = it != null
            binding.cardNumber.text = getString(R.string.card_number_text, it?.number?.takeLast(4))

            binding.btnNext.isEnabled = it != null
        })
    }

    private fun setOnResult() {
        setFragmentResultListener("cards_request") { _, bundle ->
            val result = bundle.getInt("cards_result")
            fragViewModel.onCardsResult(result)
        }
    }
}