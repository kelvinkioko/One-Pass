package xml.one.pass.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import xml.one.pass.R
import xml.one.pass.databinding.HomeFragmentBinding
import xml.one.pass.extension.viewBinding

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment) {

    private val binding by viewBinding(HomeFragmentBinding::bind)
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpClickListener()
        setUpObservers()
    }

    private fun setUpClickListener() {
        binding.apply {
            searchInput.setEndIconOnClickListener {
                searchInput.editText?.setText("")
            }
        }
    }

    private fun setUpObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.homeUiState.collect { state ->
                when (state) {
                    HomeUiState.HomeState -> {}
                    is HomeUiState.PasswordCompromised ->
                        binding.compromisedPasswordsCount.text = state.passwordCompromised
                    is HomeUiState.PasswordStored ->
                        binding.storedPasswordsCount.text = state.passwordStored
                    is HomeUiState.Passwords -> {}
                }
            }
        }
    }
}
