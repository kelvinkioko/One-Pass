package xml.one.pass.presentation.password.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import xml.one.pass.R
import xml.one.pass.common.GlobalAction
import xml.one.pass.databinding.PasswordDetailsFragmentBinding
import xml.one.pass.domain.model.PasswordModel
import xml.one.pass.extension.copyPassword
import xml.one.pass.extension.maskString
import xml.one.pass.extension.observeEvent
import xml.one.pass.extension.observeState
import xml.one.pass.extension.viewBinding

@AndroidEntryPoint
class PasswordDetailsFragment : Fragment(R.layout.password_details_fragment) {

    private val binding by viewBinding(PasswordDetailsFragmentBinding::bind)
    private val viewModel: PasswordDetailsViewModel by viewModels()
    private val args: PasswordDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolBar()
        setClickListener()
        setObserver()

        viewModel.loadPasswordDetails(passwordId = args.passwordID)
    }

    private fun setToolBar() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setClickListener() {
        binding.apply {
            updateAction.setOnClickListener {
                viewModel.navigateToEditPasswordDetails()
            }

            deleteAction.setOnClickListener {
                // TODO Create dialog extension for warnings and errors
            }
        }
    }

    private fun setObserver() {
        viewModel.uiState.observeState(this, Lifecycle.State.STARTED) { state ->
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

        viewModel.action.observeEvent(
            lifecycleOwner = this,
            lifecycleState = Lifecycle.State.STARTED
        ) { action ->
            when (action) {
                is GlobalAction.Navigate -> findNavController().navigate(action.directions)
            }
        }
    }

    private fun renderPasswordDetails(password: PasswordModel) {
        binding.apply {
            welcomeTitle.text = password.siteName
            websiteValue.text = password.url
            userNameValue.text = password.userName
            emailAddressValue.text = password.email
            passwordValue.text = password.password.maskString()
            phoneNumberValue.text = password.phoneNumber
            updatedValue.text = password.timeUpdated.asDate()
            createdValue.text = password.timeCreated.asDate()

            passwordShowHideAction.setOnCheckedChangeListener { _, isChecked ->
                passwordValue.text = if (isChecked)
                    password.password
                else
                    password.password.maskString()
            }

            passwordCopyAction.setOnCheckedChangeListener { _, _ ->
                password.password.copyPassword(context = requireContext())
            }
        }
    }
}
