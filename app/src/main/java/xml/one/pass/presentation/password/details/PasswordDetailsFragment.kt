package xml.one.pass.presentation.password.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import xml.one.pass.R
import xml.one.pass.databinding.PasswordDetailsFragmentBinding
import xml.one.pass.domain.model.PasswordModel
import xml.one.pass.extension.viewBinding

class PasswordDetailsFragment : Fragment(R.layout.password_details_fragment) {

    private val binding by viewBinding(PasswordDetailsFragmentBinding::bind)
    private val viewModel: PasswordDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolBar()
        setClickListener()
        setObserver()
    }

    private fun setToolBar() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setClickListener() {
        binding.apply {
            updateAction.setOnClickListener {
            }

            deleteAction.setOnClickListener {
                // TODO Create dialog extension for warnings and errors
            }
        }
    }

    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    when (state) {
                        PasswordDetailsUiState.DefaultState -> Unit
                        is PasswordDetailsUiState.Error ->
                            Snackbar.make(
                                binding.root,
                                state.errorMessage.asString(context = requireContext()),
                                Snackbar.LENGTH_LONG
                            ).show()
                        is PasswordDetailsUiState.PasswordDetails ->
                            renderPasswordDetails(password = state.password)
                    }
                }
            }
        }
    }

    private fun renderPasswordDetails(password: PasswordModel) {
        binding.apply {
            welcomeTitle.text = password.siteName
            websiteValue.text = password.url
            userNameValue.text = password.userName
            emailAddressValue.text = password.email
            passwordValue.text = password.password
            phoneNumberValue.text = password.phoneNumber
            updatedValue.text = password.userName
            createdValue.text = password.userName
        }
    }
}
