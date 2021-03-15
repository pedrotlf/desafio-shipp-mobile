package br.com.pedrotlf.desafioshippmobile.establishments

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import br.com.pedrotlf.desafioshippmobile.BaseFragment
import br.com.pedrotlf.desafioshippmobile.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_establishment_order_description.*
import kotlinx.android.synthetic.main.item_places.*
import org.jetbrains.anko.support.v4.act


class EstablishmentOrderDescriptionFragment: BaseFragment() {
    private var btnNextClicked: (EstablishmentOrder) -> Unit = {}

    private var establishmentOrder: EstablishmentOrder? = null
    
    companion object{
        fun getInstance(btnNextClicked: (EstablishmentOrder) -> Unit): EstablishmentOrderDescriptionFragment{
            val frag = EstablishmentOrderDescriptionFragment()
            frag.btnNextClicked = btnNextClicked
            return frag
        }
    }

    fun setInfo(establishmentOrder: EstablishmentOrder){
        val oldEstablishmentId: String? = this.establishmentOrder?.id.apply{}
        this.establishmentOrder = establishmentOrder
        applyInfo(oldEstablishmentId)
    }

    private fun applyInfo(oldEstablishmentId: String? = null) {
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
                    act.resources.displayMetrics
            ).toInt()
            Glide.with(act)
                    .load(establishmentOrder?.photo)
                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(px)))
                    .into(image)
        }

        if(oldEstablishmentId != establishmentOrder?.id) {
            orderDetails?.setText("")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setView(R.layout.fragment_establishment_order_description)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnNext.setOnClickListener {
            if(!orderDetails.text.isNullOrBlank()) {
                establishmentOrder?.orderDetails = orderDetails.text.toString()
                establishmentOrder?.let { order -> btnNextClicked(order) }
            }
        }

        orderDetails.doOnTextChanged { text, _, _, _ ->
            btnNext.isEnabled = !text.isNullOrBlank()
        }

        btnBack.setOnClickListener { act.onBackPressed() }

        applyInfo()
    }
}