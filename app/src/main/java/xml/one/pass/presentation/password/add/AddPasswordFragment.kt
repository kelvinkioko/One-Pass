package xml.one.pass.presentation.password.add

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import xml.one.pass.R
import xml.one.pass.databinding.PasswordAddFragmentBinding
import xml.one.pass.extension.viewBinding

@AndroidEntryPoint
class AddPasswordFragment : Fragment(R.layout.password_add_fragment) {

    private val binding by viewBinding(PasswordAddFragmentBinding::bind)
    private val viewModel: AddPasswordViewModel by viewModels()
    private val args: AddPasswordFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.passwordID = args.passwordID

        setToolbar()
        setUpClickListener()
        setUpObservers()
    }

    private fun setToolbar() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setUpClickListener() {
        binding.apply {
            updateAction.setOnClickListener {
                if (nameInput.editText?.text.toString().isEmpty())
                    nameInput.error = "Name is required!"
                else if (passwordInput.editText?.text.toString().isEmpty())
                    passwordInput.error = "Password is required!"
                else if (
                    userNameInput.editText?.text.toString().isEmpty() &&
                    emailAddressInput.editText?.text.toString().isEmpty() &&
                    phoneNumberInput.editText?.text.toString().isEmpty()
                ) {
                    userNameInput.error = "Email/username/phone number is required!"
                    emailAddressInput.error = "Email/username/phone number is required!"
                    phoneNumberInput.error = "Email/username/phone number is required!"
                } else {
                    viewModel.savePassword(
                        siteName = nameInput.editText?.text.toString().trim(),
                        url = websiteInput.editText?.text.toString().trim(),
                        userName = userNameInput.editText?.text.toString().trim(),
                        email = emailAddressInput.editText?.text.toString().trim(),
                        password = passwordInput.editText?.text.toString().trim(),
                        phoneNumber = phoneNumberInput.editText?.text.toString().trim()
                    )
                }
            }
        }
    }

    private fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    when (state) {
                        is AddPasswordUiState.Loading -> {}
                        AddPasswordUiState.Success ->
                            findNavController().navigateUp()
                        is AddPasswordUiState.Error ->
                            Snackbar.make(
                                binding.root,
                                state.errorMessage.asString(context = requireContext()),
                                Snackbar.LENGTH_LONG
                            ).show()
                    }
                }
            }
        }
    }

    private fun setInputObservers() {
        binding.apply {
            nameInput.editText?.doAfterTextChanged {
                nameInput.error = null
            }
            websiteInput.editText?.doAfterTextChanged {
                nameInput.error = null
            }
            userNameInput.editText?.doAfterTextChanged {
                invalidateUserIdentifierError()
            }
            emailAddressInput.editText?.doAfterTextChanged {
                invalidateUserIdentifierError()
            }
            passwordInput.editText?.doAfterTextChanged {
                nameInput.error = null
            }
            phoneNumberInput.editText?.doAfterTextChanged {
                invalidateUserIdentifierError()
            }
        }
    }

    private fun invalidateUserIdentifierError() {
        binding.apply {
            userNameInput.error = null
            emailAddressInput.error = null
            phoneNumberInput.error = null
        }
    }

    private fun removeInputObservers() {
        binding.apply {
            nameInput.editText?.doAfterTextChanged {}
            websiteInput.editText?.doAfterTextChanged {}
            userNameInput.editText?.doAfterTextChanged {}
            emailAddressInput.editText?.doAfterTextChanged {}
            passwordInput.editText?.doAfterTextChanged {}
            phoneNumberInput.editText?.doAfterTextChanged {}
        }
    }

    override fun onResume() {
        super.onResume()
        setInputObservers()
    }

    override fun onPause() {
        super.onPause()
        removeInputObservers()
    }
}
