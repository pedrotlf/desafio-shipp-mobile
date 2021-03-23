package br.com.pedrotlf.desafioshippmobile.ui.order.resume

import androidx.lifecycle.*
import br.com.pedrotlf.desafioshippmobile.data.card.Card
import br.com.pedrotlf.desafioshippmobile.data.card.CardDao
import br.com.pedrotlf.desafioshippmobile.data.order.Order
import br.com.pedrotlf.desafioshippmobile.data.order.OrderRepository
import br.com.pedrotlf.desafioshippmobile.utils.DataState
import br.com.pedrotlf.desafioshippmobile.utils.SELECT_CARD_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderResumeViewModel @Inject constructor(
        private val orderRepository: OrderRepository,
        cardDao: CardDao
) : ViewModel() {

    val selectedCard = cardDao.getSelectedCard().asLiveData()

    private val orderResumeEventsChannel = Channel<OrderResumeEvent>()
    val orderResumeEvent = orderResumeEventsChannel.receiveAsFlow()

    private val _checkoutState = MutableLiveData<DataState<Order>>()

    val checkoutState: LiveData<DataState<Order>>
        get() = _checkoutState
    fun onNextClicked(order: Order) {
        selectedCard.value?.let{ requestCheckout(order, it) }
    }

    fun onSelectCardClicked() = viewModelScope.launch {
        orderResumeEventsChannel.send(OrderResumeEvent.GoToCards)
    }

    fun goToCheckout(order: Order) = viewModelScope.launch {
        orderResumeEventsChannel.send(OrderResumeEvent.GoToCheckout(order))
    }

    fun onCardsResult(result: Int)  {
        when(result){
            SELECT_CARD_RESULT_OK -> {
                showMessage("Cartão selecionado!")
            }
        }
    }

    private fun showMessage(msg: String) = viewModelScope.launch {
        orderResumeEventsChannel.send(OrderResumeEvent.ShowCardSelectedMessage(msg))
    }

    private fun requestCheckout(order: Order, card: Card) = viewModelScope.launch {
        orderRepository.getCheckout(order, card).onEach {
            _checkoutState.value = it
        }.launchIn(viewModelScope)
    }

    sealed class OrderResumeEvent{
        data class ShowCardSelectedMessage(val msg: String): OrderResumeEvent()
        data class GoToCheckout(val order: Order): OrderResumeEvent()
        object GoToCards: OrderResumeEvent()
    }
}
