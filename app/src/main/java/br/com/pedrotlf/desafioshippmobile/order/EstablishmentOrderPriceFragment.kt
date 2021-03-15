package br.com.pedrotlf.desafioshippmobile.order

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import br.com.pedrotlf.desafioshippmobile.EstablishmentOrder
import br.com.pedrotlf.desafioshippmobile.R
import br.com.pedrotlf.desafioshippmobile.utils.BaseFragment
import br.com.pedrotlf.desafioshippmobile.utils.MoneyTextWatcher
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_establishment_order_price.*
import kotlinx.android.synthetic.main.item_places.*
import org.jetbrains.anko.support.v4.act
import java.text.NumberFormat


class EstablishmentOrderPriceFragment: BaseFragment() {
    private var btnNextClicked: (EstablishmentOrder) -> Unit = {}

    private var establishmentOrder: EstablishmentOrder? = null

    companion object{
        fun getInstance(btnNextClicked: (EstablishmentOrder) -> Unit): EstablishmentOrderPriceFragment {
            val frag = EstablishmentOrderPriceFragment()
            frag.btnNextClicked = btnNextClicked
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
                    act.resources.displayMetrics
            ).toInt()
            Glide.with(act)
                    .load(establishmentOrder?.photo)
                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(px)))
                    .into(image)
        }
        description.visibility = View.VISIBLE
        description.text = establishmentOrder?.orderDetails

        inputPrice.setText("0")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setView(R.layout.fragment_establishment_order_price)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputPrice.addTextChangedListener(MoneyTextWatcher(inputPrice) { isNonZero ->
            btnNext.isEnabled = isNonZero
        })

        btnNext.setOnClickListener {
            establishmentOrder?.price = NumberFormat.getCurrencyInstance().parse(inputPrice.text.toString())?.toDouble()
            val focusedView = act.currentFocus
            focusedView?.clearFocus()
            if (focusedView != null) {
                val imm: InputMethodManager? = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.hideSoftInputFromWindow(focusedView.windowToken, 0)
            }

            establishmentOrder?.let { order -> btnNextClicked(order) }
        }

        btnBack.setOnClickListener { act.onBackPressed() }

        applyInfo()
    }
}