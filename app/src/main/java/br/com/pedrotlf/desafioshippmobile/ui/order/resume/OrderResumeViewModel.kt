package br.com.pedrotlf.desafioshippmobile.ui.order.resume

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pedrotlf.desafioshippmobile.data.Order
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class OrderResumeViewModel @AssistedInject constructor(
        @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val orderResumeEventsChannel = Channel<OrderResumeEvents>()
    val orderResumeEvents = orderResumeEventsChannel.receiveAsFlow()

    sealed class OrderResumeEvents{
        data class NavigateToSuccess(val order: Order) : OrderResumeEvents()
    }

    fun onNextClicked(order: Order?) {
        if (order != null)
            viewModelScope.launch {
                orderResumeEventsChannel.send(OrderResumeEvents.NavigateToSuccess(order))
            }
    }
}