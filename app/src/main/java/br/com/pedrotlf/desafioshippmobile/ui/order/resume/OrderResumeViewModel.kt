package br.com.pedrotlf.desafioshippmobile.ui.order.resume

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pedrotlf.desafioshippmobile.data.order.Order
import br.com.pedrotlf.desafioshippmobile.data.order.OrderRepository
import br.com.pedrotlf.desafioshippmobile.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderResumeViewModel @Inject constructor(
        private val orderRepository: OrderRepository
) : ViewModel() {

    sealed class OrderResumeEvents{
        data class GetCheckout(val order: Order) : OrderResumeEvents()
    }

    private val _checkoutState = MutableLiveData<DataState<Order>>()
    val checkoutState: LiveData<DataState<Order>>
        get() = _checkoutState

    private fun setCheckoutState(event: OrderResumeEvents) {
        viewModelScope.launch {
            when (event) {
                is OrderResumeEvents.GetCheckout -> {
                    orderRepository.getCheckout(event.order).onEach {
                        _checkoutState.value = it
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    fun onNextClicked(order: Order) {
        setCheckoutState(OrderResumeEvents.GetCheckout(order))
    }
}