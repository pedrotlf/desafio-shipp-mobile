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

    val cardNumber = MutableStateFlow("")
    val cardExpirationDate = MutableStateFlow("")
    val cardCvv = MutableStateFlow("")
    val cardOwnerName = MutableStateFlow("")
    val cardOwnerCpf = MutableStateFlow("")

    private val _card = MutableStateFlow(Card())
    private val cardFlow = combine(
            cardNumber,
            cardExpirationDate,
            cardCvv,
            cardOwnerName,
            cardOwnerCpf
    ){ number, date, cvv, name, cpf ->
        listOf(number, date, cvv, name, cpf)
    }.flatMapLatest {
        _card.value = Card(it[0], it[1], it[2], it[3], it[4])
        _card
    }

    val card = cardFlow.asLiveData()
}