package br.com.pedrotlf.desafioshippmobile.ui.cards.addedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import br.com.pedrotlf.desafioshippmobile.data.card.Card
import br.com.pedrotlf.desafioshippmobile.data.card.CardDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class AddEditCardViewModel @Inject constructor(
        private val cardDao: CardDao,
        private val state: SavedStateHandle
): ViewModel() {

    val cardNumber = MutableStateFlow(Pair("", ""))
    val cardExpirationDate = MutableStateFlow(Pair("", ""))
    val cardCvv = MutableStateFlow("")
    val cardOwnerName = MutableStateFlow("")
    val cardOwnerCpf = MutableStateFlow(Pair("", ""))

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
            Card(number.first, date.first, cvv, name, cpf.first),
            Card(number.second, date.second, cvv, name, cpf.second),
        )
        _cardFormattedData
    }
    val cardFormatted = cardFormattedFlow.asLiveData()

    data class CardValues<T1,T2,T3,T4,T5>(val t1: T1, val t2: T2, val t3: T3, val t4: T4, val t5: T5)
}