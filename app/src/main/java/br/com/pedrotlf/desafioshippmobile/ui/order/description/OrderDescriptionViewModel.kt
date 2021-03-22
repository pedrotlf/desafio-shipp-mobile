package br.com.pedrotlf.desafioshippmobile.ui.order.description

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pedrotlf.desafioshippmobile.data.Order
import br.com.pedrotlf.desafioshippmobile.data.Place
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class OrderDescriptionViewModel @AssistedInject constructor(
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val orderDescriptionEventsChannel = Channel<OrderDescriptionEvents>()
    val orderDescriptionEvents = orderDescriptionEventsChannel.receiveAsFlow()

    val orderDetails = state.getLiveData("orderDetails", "")

    sealed class OrderDescriptionEvents{
        data class NavigateToPrices(val order: Order) : OrderDescriptionEvents()
    }

    fun onNextClicked(order: Order?) {
        if (order != null)
            viewModelScope.launch {
                orderDescriptionEventsChannel.send(OrderDescriptionEvents.NavigateToPrices(order.apply { orderDetails = this@OrderDescriptionViewModel.orderDetails.value ?: "" }))
            }
    }
}