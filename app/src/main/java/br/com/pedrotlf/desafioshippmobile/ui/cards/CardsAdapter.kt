package br.com.pedrotlf.desafioshippmobile.ui.cards

import android.app.Application
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.pedrotlf.desafioshippmobile.R
import br.com.pedrotlf.desafioshippmobile.data.card.Card
import br.com.pedrotlf.desafioshippmobile.databinding.ItemCardBinding
import javax.inject.Inject

class CardsAdapter(private val onClickListener: OnItemClickListener) : ListAdapter<Card, CardsAdapter.CardViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class CardViewHolder (private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val card = getItem(position)
                        onClickListener.onItemClick(card)
                    }
                }
                btnEdit.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val card = getItem(position)
                        onClickListener.onItemEditClick(card)
                    }
                }
            }
        }

        fun bind(card: Card){
            binding.apply {
                cardNumber.text = "•••• ${card.number.takeLast(4)}"
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(card: Card)
        fun onItemEditClick(card: Card)
    }

    class DiffCallback: DiffUtil.ItemCallback<Card>(){
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean =
                oldItem == newItem

    }
}