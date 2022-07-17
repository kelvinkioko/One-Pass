package xml.one.pass.presentation.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import xml.one.pass.R
import xml.one.pass.databinding.HomeFragmentBinding
import xml.one.pass.domain.model.PasswordModel
import xml.one.pass.extension.setNullableAdapter
import xml.one.pass.extension.viewBinding
import xml.one.pass.presentation.home.adapter.PasswordsAdapter

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment) {

    private val binding by viewBinding(HomeFragmentBinding::bind)
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpClickListener()
        setUpObservers()
        setUpList()
    }

    private fun setUpClickListener() {
        binding.apply {
            searchInput.setEndIconOnClickListener {
                searchInput.editText?.setText("")
            }
        }
    }

    private fun setUpObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    when (state) {
                        HomeUiState.HomeState -> Unit
                        is HomeUiState.PasswordCompromised ->
                            binding.compromisedPasswordsCount.text = state.passwordCompromised
                        is HomeUiState.PasswordStored ->
                            binding.storedPasswordsCount.text = state.passwordStored
                        is HomeUiState.Passwords ->
                            renderPasswords(passwords = state.passwords)
                        is HomeUiState.PasswordDetails ->
                            findNavController().navigate(state.detailsDestination)
                    }
                }
            }
        }
    }

    private val passwordsAdapter: PasswordsAdapter by lazy {
        PasswordsAdapter(
            passwordID = { passwordID ->
                viewModel.navigateToPasswordDetails(passwordID = passwordID)
            }
        )
    }

    private fun setUpList() {
        binding.passwordsList.setNullableAdapter(passwordsAdapter)
    }

    private fun renderPasswords(passwords: List<PasswordModel>) {
        binding.apply {
            contentGroup.isVisible = passwords.isNotEmpty()
            emptyGroup.isVisible = passwords.isEmpty()
        }
        passwordsAdapter.submitList(passwords)
    }

    override fun onResume() {
        super.onResume()
        viewModel.setUpHomePage()
    }
}
