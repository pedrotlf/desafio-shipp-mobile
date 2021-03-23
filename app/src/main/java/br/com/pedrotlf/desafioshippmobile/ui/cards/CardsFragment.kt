package br.com.pedrotlf.desafioshippmobile.ui.cards

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.pedrotlf.desafioshippmobile.R
import br.com.pedrotlf.desafioshippmobile.data.card.Card
import br.com.pedrotlf.desafioshippmobile.databinding.FragmentCardsBinding
import br.com.pedrotlf.desafioshippmobile.utils.CustomDividerItemDecoration
import br.com.pedrotlf.desafioshippmobile.utils.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CardsFragment : Fragment(R.layout.fragment_cards), CardsAdapter.OnItemClickListener {

    private val viewModel: CardsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCardsBinding.bind(view)

        val cardsAdapter = CardsAdapter(this)

        binding.apply {
            configureRecyclerView(cardsAdapter)

            addCard.setOnClickListener {
                viewModel.onAddClicked()
            }
        }

        viewModel.cards.observe(viewLifecycleOwner){
            cardsAdapter.submitList(it)
            binding.cardsEmptyLayout.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.selectedCard.observe(viewLifecycleOwner){}

        registerEventCollector()
        setOnResult()
    }

    private fun FragmentCardsBinding.configureRecyclerView(cardsAdapter: CardsAdapter) {
        recyclerCards.apply {
            adapter = cardsAdapter
            layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val card = cardsAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onCardSwiped(card)
                }
            }).attachToRecyclerView(recyclerCards)

            addItemDecoration(CustomDividerItemDecoration(ContextCompat.getDrawable(requireContext(), R.drawable.divider)))
        }
    }

    private fun registerEventCollector() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.cardsEvent.collect { event ->
                @Suppress("IMPLICIT_CAST_TO_ANY")
                when (event) {
                    is CardsViewModel.CardEvent.ShowCardSavedMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is CardsViewModel.CardEvent.GoToCreateCard -> {
                        val action =
                            CardsFragmentDirections.actionCardsFragmentToAddEditCardFragment()
                        findNavController().navigate(action)
                    }
                    is CardsViewModel.CardEvent.GoToUpdateCard -> {
                        val action =
                            CardsFragmentDirections.actionCardsFragmentToAddEditCardFragment(event.card)
                        findNavController().navigate(action)
                    }
                    is CardsViewModel.CardEvent.ShowCardDeletedMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG)
                            .setAction("DESFAZER") {
                                viewModel.onUndoDeleteClicked(event.card)
                            }.show()
                    }
                    is CardsViewModel.CardEvent.NavigateBackWithResult -> {
                        setFragmentResult(
                            "cards_request",
                            bundleOf("cards_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }
    }

    private fun setOnResult() {
        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }
    }

    override fun onItemClick(card: Card) {
        viewModel.onCardClicked(card)
    }

    override fun onItemEditClick(card: Card) {
        viewModel.onEditClicked(card)
    }
}