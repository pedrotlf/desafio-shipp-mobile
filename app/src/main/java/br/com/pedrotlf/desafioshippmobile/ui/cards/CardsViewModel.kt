package br.com.pedrotlf.desafioshippmobile.ui.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import br.com.pedrotlf.desafioshippmobile.data.card.Card
import br.com.pedrotlf.desafioshippmobile.data.card.CardDao
import br.com.pedrotlf.desafioshippmobile.utils.ADD_CARD_RESULT_OK
import br.com.pedrotlf.desafioshippmobile.utils.EDIT_CARD_RESULT_OK
import br.com.pedrotlf.desafioshippmobile.utils.SELECT_CARD_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val cardDao: CardDao
) : ViewModel() {

    val cards = cardDao.getCards().asLiveData()

    val selectedCard = cardDao.getSelectedCard().asLiveData()

    private val cardEventChannel = Channel<CardEvent>()
    val cardsEvent = cardEventChannel.receiveAsFlow()

    fun onAddClicked() = viewModelScope.launch {
        cardEventChannel.send(CardEvent.GoToCreateCard)
    }

    fun onEditClicked(card: Card) = viewModelScope.launch {
        cardEventChannel.send(CardEvent.GoToUpdateCard(card))
    }

    fun onCardClicked(card: Card) = viewModelScope.launch{
        val oldSelectedCard = selectedCard.value
        if(oldSelectedCard != null)
            cardDao.update(oldSelectedCard.copy(selected = false))

        val clickedCard = card.copy(selected = true)
        cardDao.update(clickedCard)

        cardEventChannel.send(CardEvent.NavigateBackWithResult(SELECT_CARD_RESULT_OK))
    }

    fun onCardSwiped(card: Card) = viewModelScope.launch {
        cardDao.delete(card)
        cardEventChannel.send(CardEvent.ShowCardDeletedMessage("Cartão excluído", card))
    }

    fun onUndoDeleteClicked(card: Card) = viewModelScope.launch {
        cardDao.insert(card)
    }

    fun onAddEditResult(result: Int) {
        when(result){
            ADD_CARD_RESULT_OK -> showCardSavedMessage("Cartão adicionado!")
            EDIT_CARD_RESULT_OK -> showCardSavedMessage("Os dados do seu cartão foram atualizados!")
        }
    }

    private fun showCardSavedMessage(msg: String) = viewModelScope.launch {
        cardEventChannel.send(CardEvent.ShowCardSavedMessage(msg))
    }

    sealed class CardEvent{
        data class ShowCardSavedMessage(val msg: String): CardEvent()
        data class GoToUpdateCard(val card: Card): CardEvent()
        data class ShowCardDeletedMessage(val msg: String, val card: Card): CardEvent()
        data class NavigateBackWithResult(val result: Int): CardEvent()
        object GoToCreateCard: CardEvent()
    }
}