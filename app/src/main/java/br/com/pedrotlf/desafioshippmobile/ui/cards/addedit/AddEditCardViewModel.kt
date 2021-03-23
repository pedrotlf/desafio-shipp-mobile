package br.com.pedrotlf.desafioshippmobile.ui.cards.addedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import br.com.pedrotlf.desafioshippmobile.data.card.Card
import br.com.pedrotlf.desafioshippmobile.data.card.CardDao
import br.com.pedrotlf.desafioshippmobile.utils.ADD_CARD_RESULT_OK
import br.com.pedrotlf.desafioshippmobile.utils.EDIT_CARD_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditCardViewModel @Inject constructor(
        private val cardDao: CardDao,
        state: SavedStateHandle
): ViewModel() {

    val card = state.get<Card>("card")

    val cardNumber = MutableStateFlow(Pair("", ""))
    val cardExpirationDate = MutableStateFlow("")
    val cardCvv = MutableStateFlow("")
    val cardOwnerName = MutableStateFlow("")
    val cardOwnerCpf = MutableStateFlow(Pair("", ""))

    init {
        val number = card?.number ?: ""
        cardNumber.value = Pair(number, "")

        cardExpirationDate.value = card?.expirationDate ?: ""

        cardCvv.value = card?.cvv ?: ""

        cardOwnerName.value = card?.ownerName ?: ""

        val cpf = card?.ownerCpf ?: ""
        cardOwnerCpf.value = Pair(cpf, "")
    }

    data class CardValues<T1,T2,T3,T4,T5>(val t1: T1, val t2: T2, val t3: T3, val t4: T4, val t5: T5)
    private val _cardFormattedData = MutableStateFlow(Pair(Card(), Card()))
    private val cardFormattedFlow = combine(
            cardNumber,
            cardExpirationDate,
            cardCvv,
            cardOwnerName,
            cardOwnerCpf
    ){ number, date, cvv, name, cpf ->
        CardValues(number, date, cvv, name, cpf)
    }.flatMapLatest { (number, date, cvv, name, cpf) ->
        _cardFormattedData.value = Pair(
            Card(number.first, date, cvv, name, cpf.first),
            Card(number.second, date, cvv, name, cpf.second),
        )
        _cardFormattedData
    }
    val cardFormatted = cardFormattedFlow.asLiveData()

    private val addEditCardEventChannel = Channel<AddEditCardEvent>()
    val addEditCardEvent = addEditCardEventChannel.receiveAsFlow()

    fun onSaveClicked(){
        when {
            cardNumber.value.first.length < 16 -> showInvalidInputMessage("Preencha o número do cartão corretamente")
            cardExpirationDate.value.length < 5 -> showInvalidInputMessage("Preencha a data de validade do cartão corretamente")
            cardCvv.value.length < 3 -> showInvalidInputMessage("Preencha o código CVV do cartão corretamente")
            cardOwnerName.value.isEmpty() -> showInvalidInputMessage("Preencha o nome do titular do cartão corretamente")
            cardOwnerCpf.value.first.length < 11 -> showInvalidInputMessage("Preencha o CPF do titular do cartão corretamente")

            else -> {
                if(card != null) {
                    val updatedCard = card.copy(
                        number = cardNumber.value.first,
                        expirationDate = cardExpirationDate.value,
                        cvv = cardCvv.value,
                        ownerName = cardOwnerName.value,
                        ownerCpf = cardOwnerCpf.value.first
                    )
                    updateCard(updatedCard)
                } else {
                    val newCard = Card(
                        number = cardNumber.value.first,
                        expirationDate = cardExpirationDate.value,
                        cvv = cardCvv.value,
                        ownerName = cardOwnerName.value,
                        ownerCpf = cardOwnerCpf.value.first
                    )
                    createCard(newCard)
                }
            }
        }
    }

    private fun updateCard(card: Card) = viewModelScope.launch {
        cardDao.update(card)
        addEditCardEventChannel.send(AddEditCardEvent.NavigateBackWithResult(EDIT_CARD_RESULT_OK))
    }

    private fun createCard(card: Card) = viewModelScope.launch {
        cardDao.insert(card)
        addEditCardEventChannel.send(AddEditCardEvent.NavigateBackWithResult(ADD_CARD_RESULT_OK))
    }

    private fun showInvalidInputMessage(msg: String) = viewModelScope.launch {
        addEditCardEventChannel.send(AddEditCardEvent.ShowInvalidInput(msg))
    }

    sealed class AddEditCardEvent {
        data class ShowInvalidInput(val msg: String): AddEditCardEvent()
        data class NavigateBackWithResult(val result: Int): AddEditCardEvent()
    }
}