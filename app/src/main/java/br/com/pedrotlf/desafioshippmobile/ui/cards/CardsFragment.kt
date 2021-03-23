package br.com.pedrotlf.desafioshippmobile.ui.cards

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.pedrotlf.desafioshippmobile.R
import br.com.pedrotlf.desafioshippmobile.databinding.FragmentCardsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardsFragment : Fragment(R.layout.fragment_cards) {

    private val viewModel: CardsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCardsBinding.bind(view)

        val cardsAdapter = CardsAdapter()

        binding.apply {
            recyclerCards.apply {
                adapter = cardsAdapter
                layoutManager = object: LinearLayoutManager(requireContext()) { override fun canScrollVertically(): Boolean { return false } }
            }

            addCard.setOnClickListener {
                val action = CardsFragmentDirections.actionCardsFragmentToAddEditCardFragment(null)
                findNavController().navigate(action)
            }
        }

        viewModel.cards.observe(viewLifecycleOwner){
            cardsAdapter.submitList(it)
        }
    }
}