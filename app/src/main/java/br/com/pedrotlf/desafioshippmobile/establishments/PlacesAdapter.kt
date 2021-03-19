package br.com.pedrotlf.desafioshippmobile.establishments

import android.content.Context
import android.graphics.Bitmap
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.pedrotlf.desafioshippmobile.EstablishmentOrder
import br.com.pedrotlf.desafioshippmobile.databinding.ItemPlacesBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import kotlinx.android.synthetic.main.item_places.view.*

class PlacesAdapter(
    val context: Context,
    val updateDetails: (String, (LatLng?, Bitmap?)->Unit) -> Unit,
    val onPlaceClicked: (EstablishmentOrder) -> Unit
) : ListAdapter<AutocompletePrediction, PlacesAdapter.PlacesViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder {
        val binding = ItemPlacesBinding.inflate(LayoutInflater.from(context), parent, false)
        return PlacesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(context, place, updateDetails, onPlaceClicked)
    }

    class PlacesViewHolder(private val binding: ItemPlacesBinding) : RecyclerView.ViewHolder(binding.root) {
        private var photo: Bitmap? = null
        private var id: String? = null
        private var latLng: LatLng? = null

        fun bind(context: Context, place: AutocompletePrediction, updateDetails: (String, (LatLng?, Bitmap?)->Unit) -> Unit, onPlaceClicked: (EstablishmentOrder) -> Unit){
            binding.apply {
                val name = place.getPrimaryText(null)
                placeTitle.text = name

                val enderecoDetails = place.getSecondaryText(null).split(" - ")
                val endereco = enderecoDetails[0]
                address.text = endereco
                val bairro: String? = try {
                    city.text = enderecoDetails[1]
                    city.visibility = View.VISIBLE
                    enderecoDetails[1]
                }catch(e: IndexOutOfBoundsException){
                    city.visibility = View.GONE
                    null
                }

                if(this@PlacesViewHolder.id != place.placeId) {
                    image.setImageDrawable(null)
                    imageProgress.visibility = View.VISIBLE
                    this@PlacesViewHolder.id = place.placeId
                    updateDetails(place.placeId) { latLng, photoBitmap ->
                        this@PlacesViewHolder.latLng = latLng
                        imageProgress.visibility = View.GONE
                        updatePhoto(context, photoBitmap)
                    }
                }

                card.setOnClickListener { onPlaceClicked(EstablishmentOrder(place.placeId, name.toString(), endereco, bairro, this@PlacesViewHolder.photo, this@PlacesViewHolder.latLng)) }
            }
        }

        private fun ItemPlacesBinding.updatePhoto(context: Context, photoBitmap: Bitmap?) {
            val dip = 5f
            val px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                context.resources.displayMetrics
            ).toInt()
            this@PlacesViewHolder.photo = photoBitmap
            Glide.with(context)
                .load(photoBitmap)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(px)))
                .into(image)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AutocompletePrediction>(){
        override fun areItemsTheSame(oldItem: AutocompletePrediction, newItem: AutocompletePrediction): Boolean =
            oldItem.placeId == newItem.placeId

        override fun areContentsTheSame(oldItem: AutocompletePrediction, newItem: AutocompletePrediction): Boolean {
            return (oldItem.placeId == newItem.placeId
                    && oldItem.getPrimaryText(null) == newItem.getPrimaryText(null)
                    && oldItem.getSecondaryText(null) == newItem.getSecondaryText(null)
                    )
        }
    }
}