package br.com.pedrotlf.desafioshippmobile.ui.order.price

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
import java.text.NumberFormat

class OrderPriceViewModel @AssistedInject constructor(
        @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val orderDescriptionEventsChannel = Channel<OrderPriceEvents>()
    val orderDescriptionEvents = orderDescriptionEventsChannel.receiveAsFlow()

    val orderPrice = state.getLiveData("orderPrice", "")

    sealed class OrderPriceEvents{
        data class NavigateToResume(val order: Order) : OrderPriceEvents()
    }

    fun onNextClicked(order: Order?) {
        if (order != null)
            viewModelScope.launch {
                orderDescriptionEventsChannel.send(
                        OrderPriceEvents.NavigateToResume(order.apply {
                            price = NumberFormat.getCurrencyInstance().parse(orderPrice.value?.toString() ?: "")?.toDouble()
                        })
                )
            }
    }
}