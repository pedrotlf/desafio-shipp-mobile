package br.com.pedrotlf.desafioshippmobile.order

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.pedrotlf.desafioshippmobile.EstablishmentOrder
import br.com.pedrotlf.desafioshippmobile.R
import br.com.pedrotlf.desafioshippmobile.utils.BaseFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_order_success.*
import kotlinx.android.synthetic.main.item_place_order.*
import java.text.NumberFormat

class OrderSuccessFragment: BaseFragment() {
    private var btnConfirmClicked: () -> Unit = {}

    private var establishmentOrder: EstablishmentOrder? = null

    companion object{
        fun getInstance(btnConfirmClicked: () -> Unit): OrderSuccessFragment {
            val frag = OrderSuccessFragment()
            frag.btnConfirmClicked = btnConfirmClicked
            return frag
        }
    }

    fun setInfo(establishmentOrder: EstablishmentOrder){
        this.establishmentOrder = establishmentOrder
        applyInfo()
    }

    private fun applyInfo() {
        placeTitle?.text = establishmentOrder?.name
        address?.text = establishmentOrder?.address
        if (!establishmentOrder?.city.isNullOrBlank()) {
            city?.visibility = View.VISIBLE
            city?.text = establishmentOrder?.city
        } else {
            city?.visibility = View.GONE
        }
        if (image != null) {
            val dip = 5f
            val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dip,
                resources.displayMetrics
            ).toInt()
            Glide.with(this)
                .load(establishmentOrder?.photo)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(px)))
                .into(image)
        }

        description.text = establishmentOrder?.orderDetails

        val totalPrice = try {
            NumberFormat.getCurrencyInstance().format(establishmentOrder?.totalPrice)
        } catch (e: Exception){
            0
        }
        price.text = getString(R.string.establishment_order_resume_price_label, totalPrice)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setView(R.layout.fragment_order_success)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnNext.setOnClickListener {
            btnConfirmClicked()
        }

        applyInfo()
    }
}