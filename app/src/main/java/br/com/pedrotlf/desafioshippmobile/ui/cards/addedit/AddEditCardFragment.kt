package br.com.pedrotlf.desafioshippmobile.ui.cards.addedit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.com.pedrotlf.desafioshippmobile.R
import br.com.pedrotlf.desafioshippmobile.databinding.FragmentCardAddEditBinding
import com.redmadrobot.inputmask.MaskedTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.sdk27.coroutines.textChangedListener

@AndroidEntryPoint
class AddEditCardFragment : Fragment(R.layout.fragment_card_add_edit){

    private val viewModel: AddEditCardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCardAddEditBinding.bind(view)

        binding.apply {
            setFieldListeners()
            registerCardObserver()
        }
    }

    private fun FragmentCardAddEditBinding.setFieldListeners() {
        MaskedTextChangedListener.apply {
            installOn(inputCardNumber, "[0000] [0000] [0000] [0000]").apply {
                valueListener = object : MaskedTextChangedListener.ValueListener {
                    override fun onTextChanged(maskFilled: Boolean, extractedValue: String, formattedValue: String) {
                        viewModel.cardNumber.value = Pair(extractedValue, formattedValue)
                    }
                }
            }

            installOn(inputCardExpirationDate, "[00]{/}[00]").apply {
                valueListener = object : MaskedTextChangedListener.ValueListener{
                    override fun onTextChanged(maskFilled: Boolean, extractedValue: String, formattedValue: String) {
                        viewModel.cardExpirationDate.value = Pair(extractedValue, formattedValue)
                    }
                }
            }

            installOn(inputCardCvv, "[000]").apply {
                valueListener = object : MaskedTextChangedListener.ValueListener {
                    override fun onTextChanged(maskFilled: Boolean, extractedValue: String, formattedValue: String) {
                        viewModel.cardCvv.value = extractedValue
                    }
                }
            }

            installOn(inputCardOwnerCpf, "[000]{.}[000]{.}[000]{-}[00]").apply {
                valueListener = object : MaskedTextChangedListener.ValueListener {
                    override fun onTextChanged(maskFilled: Boolean, extractedValue: String, formattedValue: String) {
                        viewModel.cardOwnerCpf.value = Pair(extractedValue, formattedValue)
                    }
                }
            }
        }

        inputCardOwnerName.textChangedListener {
            this.afterTextChanged { viewModel.cardOwnerName.value = it.toString() }
        }
    }

    private fun FragmentCardAddEditBinding.registerCardObserver() {
        viewModel.cardFormatted.observe(viewLifecycleOwner) {
            card.cardNumber.text = it.second.number
            card.cardExpirationDate.text = it.second.expirationDate
            card.cardOwnerName.text = it.second.ownerName
        }
    }
}