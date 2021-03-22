package br.com.pedrotlf.desafioshippmobile.ui.order.success

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.pedrotlf.desafioshippmobile.R
import br.com.pedrotlf.desafioshippmobile.databinding.FragmentOrderSuccessBinding
import br.com.pedrotlf.desafioshippmobile.ui.order.OrderViewModel
import br.com.pedrotlf.desafioshippmobile.utils.dipFromPixels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.text.NumberFormat

class OrderSuccessFragment : Fragment(R.layout.fragment_order_success) {
    private val orderViewModel: OrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            goBackHome()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentOrderSuccessBinding.bind(view)

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

            btnNext.setOnClickListener {
                goBackHome()
            }
        }
    }

    private fun goBackHome() {
        val action = OrderSuccessFragmentDirections.actionOrderSuccessFragmentToPlacesFragment()
        findNavController().navigate(action)
    }
}