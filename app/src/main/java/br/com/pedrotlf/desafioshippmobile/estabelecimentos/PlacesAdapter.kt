package br.com.pedrotlf.desafioshippmobile.estabelecimentos

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import br.com.pedrotlf.desafioshippmobile.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.libraries.places.api.model.AutocompletePrediction
import kotlinx.android.synthetic.main.item_places.view.*

class PlacesAdapter(
    val context: FragmentActivity,
    val establishmentsViewModel: EstablishmentsViewModel,
    val onPlaceClicked: (String, String, String, String?) -> Unit
) : RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder>() {

    var places: List<AutocompletePrediction> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class PlacesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: CardView? = itemView.card
        val titulo: TextView? = itemView.title
        val endereco: TextView? = itemView.address
        val bairro: TextView? = itemView.city
        val imagem: ImageView? = itemView.image
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder =
        PlacesViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_places, parent, false)
        )

    override fun getItemCount(): Int = places.size

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        val place = places[position]
        val name = place.getPrimaryText(null)
        holder.titulo?.text = name

        val enderecoDetails = place.getSecondaryText(null).split(" - ")
        val endereco = enderecoDetails[0]
        holder.endereco?.text = endereco
        val bairro: String? = try {
            holder.bairro?.visibility = View.VISIBLE
            holder.bairro?.text = enderecoDetails[1]
            enderecoDetails[1]
        }catch(e: IndexOutOfBoundsException){
            holder.bairro?.visibility = View.GONE
            null
        }

        establishmentsViewModel.getPlaceDetails(place.placeId){_,_,photoMetadata ->
            if(photoMetadata != null)
                establishmentsViewModel.getPlacePhoto(photoMetadata){photoBitmap ->
                    holder.imagem?.let {
                        val dip = 5f
                        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            dip,
                            context.resources.displayMetrics
                        ).toInt()
                        Glide.with(context)
                            .load(photoBitmap)
                            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(px)))
                            .into(it)
                    }
                }
        }

        holder.card?.setOnClickListener { onPlaceClicked(place.placeId, name.toString(), endereco, bairro) }
    }
}