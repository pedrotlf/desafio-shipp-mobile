package br.com.pedrotlf.desafioshippmobile.ui.cards

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import br.com.pedrotlf.desafioshippmobile.data.card.CardDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
        private val cardDao: CardDao,
        private val state: SavedStateHandle
) : ViewModel() {

    val cards = cardDao.getCards().asLiveData()
}