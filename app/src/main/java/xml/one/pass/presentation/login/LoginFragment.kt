package xml.one.pass.presentation.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import xml.one.pass.R
import xml.one.pass.databinding.LoginFragmentBinding
import xml.one.pass.extension.viewBinding
import xml.one.pass.util.ConstantsUtil

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_fragment) {

    private val binding by viewBinding(LoginFragmentBinding::bind)
    private val viewModel: LoginViewModel by viewModels()

    private var emailAddress = ""
    private var password = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpOnClickListener()
        setUpObserver()
    }

    private fun setUpOnClickListener() {
        binding.apply {
            signInAction.setOnClickListener {
                viewModel.login(email = emailAddress, password = password)
            }

            signInForgot.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.toForgotPasswordFragment())
            }

            signUpAction.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.toRegisterFragment())
            }
        }
    }

    private fun setUpObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginUiState.collectLatest { state ->
                    when (state) {
                        is LoginUiState.Error ->
                            Snackbar.make(
                                binding.root,
                                state.message.asString(context = requireContext()),
                                Snackbar.LENGTH_LONG
                            ).show()
                        is LoginUiState.Loading ->
                            setLoader(isLoading = state.isLoading)
                        LoginUiState.Success ->
                            findNavController().navigate(LoginFragmentDirections.toHomeFragment())
                    }
                }
            }
        }
    }

    private fun setLoader(isLoading: Boolean) {
        binding.signActionsGroup.isVisible = !isLoading
        binding.loader.isVisible = isLoading
    }

    private fun setUpInputListener() {
        binding.apply {
            emailAddressInput.editText?.doAfterTextChanged {
                emailAddress = if (it.isNullOrEmpty()) "" else it.toString()
                validateInputs()
            }
            passwordInput.editText?.doAfterTextChanged {
                password = if (it.isNullOrEmpty()) "" else it.toString()

                passwordInput.error = if (password.length < 8) "Password limit is 8 characters"
                else null
                validateInputs()
            }
        }
    }

    private fun validateInputs() {
        binding.apply {
            val emailValidation = ConstantsUtil.EMAIL_REGEX.toRegex().matches(emailAddress)
            val passwordValidation = password.length >= 8

            signInAction.isEnabled = emailValidation && passwordValidation
        }
    }

    private fun removeInputListener() {
        binding.apply {
            emailAddressInput.editText?.doAfterTextChanged {}
            passwordInput.editText?.doAfterTextChanged {}
        }
    }

    override fun onResume() {
        super.onResume()
        setUpInputListener()
    }

    override fun onPause() {
        super.onPause()
        removeInputListener()
    }
}
