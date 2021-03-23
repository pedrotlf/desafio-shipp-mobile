package br.com.pedrotlf.desafioshippmobile.ui.cards.addedit

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.pedrotlf.desafioshippmobile.R
import br.com.pedrotlf.desafioshippmobile.databinding.FragmentCardAddEditBinding
import br.com.pedrotlf.desafioshippmobile.utils.exhaustive
import com.google.android.material.snackbar.Snackbar
import com.redmadrobot.inputmask.MaskedTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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
            btnNext.setOnClickListener {
                viewModel.onSaveClicked()
            }

            inputCardOwnerName.setText(viewModel.cardOwnerName.value)
            inputCardCvv.setText(viewModel.cardCvv.value)
            inputCardExpirationDate.setText(viewModel.cardExpirationDate.value)
            inputCardNumber.setText(viewModel.cardNumber.value.first)
            inputCardOwnerCpf.setText(viewModel.cardOwnerCpf.value.first)

            if(viewModel.card != null){
                title.text = getString(R.string.cards_addedit_title_edit)
                btnNext.text = getString(R.string.cards_addedit_btn_next_edit)
            }
        }

        registerEventCollector()
    }

    private fun registerEventCollector() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditCardEvent.collect { event ->
                @Suppress("IMPLICIT_CAST_TO_ANY")
                when (event) {
                    is AddEditCardViewModel.AddEditCardEvent.NavigateBackWithResult -> {
                        requireActivity().currentFocus?.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                    is AddEditCardViewModel.AddEditCardEvent.ShowInvalidInput -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
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
                        viewModel.cardExpirationDate.value = formattedValue
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