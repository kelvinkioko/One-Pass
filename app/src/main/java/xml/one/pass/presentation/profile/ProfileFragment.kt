package xml.one.pass.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import xml.one.pass.R
import xml.one.pass.databinding.ProfileFragmentBinding
import xml.one.pass.domain.model.AccountModel
import xml.one.pass.extension.viewBinding

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.profile_fragment) {

    private val binding by viewBinding(ProfileFragmentBinding::bind)
    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()

        binding.updateProfileAction.text = "  ${getString(R.string.update_profile)}"
        binding.masterPasswordAction.text = "  ${getString(R.string.change_master_password)}"
        binding.logoutAction.text = "  ${getString(R.string.logout)}"
    }

    private fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.profileUiState.collectLatest { state ->
                    when (state) {
                        ProfileUiState.StartState -> Unit
                        is ProfileUiState.ProfileDetails ->
                            renderProfile(accountModel = state.account)
                    }
                }
            }
        }
    }

    private fun renderProfile(accountModel: AccountModel?) {
        binding.apply {
            contentGroup.isVisible = accountModel != null
            emptyGroup.isGone = accountModel != null
            accountModel?.let { account ->
                nameValue.text = account.name
                emailValue.text = account.email
            }
        }
    }
}
