package br.com.pedrotlf.desafioshippmobile.estabelecimentos

import android.graphics.Bitmap
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import br.com.pedrotlf.desafioshippmobile.BaseFragment
import br.com.pedrotlf.desafioshippmobile.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_establishment_order_description.*
import kotlinx.android.synthetic.main.fragment_establishment_order_description.card
import kotlinx.android.synthetic.main.item_places.*
import org.jetbrains.anko.support.v4.act

class EstablishmentOrderDescriptionFragment: BaseFragment() {
    private var name: String = ""
    private var addressText: String = ""
    private var cityText: String? = null
    private var photo: Bitmap? = null

//    private var card: View? = null
//    private var title: TextView? = null
//    private var address: TextView? = null

    companion object{
        fun getInstance(): EstablishmentOrderDescriptionFragment{
            val frag = EstablishmentOrderDescriptionFragment()
            return frag
        }
    }

    fun setInfo(name: String, addressText: String, cityText: String?, photo: Bitmap?){
        this.name = name
        this.addressText = addressText
        this.cityText = cityText
        this.photo = photo
        applyInfo()
    }

    private fun applyInfo() {
        placeTitle?.text = name
        address?.text = addressText
        if (!cityText.isNullOrBlank()) {
            city?.visibility = View.VISIBLE
            city?.text = cityText
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
                    .load(photo)
                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(px)))
                    .into(image)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setView(R.layout.fragment_establishment_order_description)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyInfo()
    }
}