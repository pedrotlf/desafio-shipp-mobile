package br.com.pedrotlf.desafioshippmobile.ui.order.price

import androidx.lifecycle.*
import br.com.pedrotlf.desafioshippmobile.data.card.CardDao
import br.com.pedrotlf.desafioshippmobile.data.order.Order
import br.com.pedrotlf.desafioshippmobile.data.order.OrderRepository
import br.com.pedrotlf.desafioshippmobile.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.NumberFormat
import javax.inject.Inject

@HiltViewModel
class OrderPriceViewModel @Inject constructor(
        private val orderRepository: OrderRepository,
        state: SavedStateHandle
) : ViewModel() {
    val orderPrice = state.getLiveData("orderPrice", "")

    sealed class OrderPriceEvents{
        data class GetResume(val order: Order) : OrderPriceEvents()
        object None : OrderPriceEvents()
    }

    private val _validationState = MutableLiveData<DataState<Order>?>()
    val validationState: LiveData<DataState<Order>?>
        get() = _validationState

    private fun setValidationState(event: OrderPriceEvents){
        viewModelScope.launch {
            when(event){
                is OrderPriceEvents.GetResume -> {
                    orderRepository.getResumeValidation(event.order.apply {
                        price = NumberFormat.getCurrencyInstance().parse(orderPrice.value?.toString() ?: "")?.toDouble()
                    }).onEach {
                        _validationState.value = it
                    }.onCompletion {
                        _validationState.value = null
                    }.launchIn(viewModelScope)
                }
                else -> {
                    //do Nothing
                }
            }
        }
    }

    fun onNextClicked(order: Order){
        setValidationState(OrderPriceEvents.GetResume(order))
    }
}