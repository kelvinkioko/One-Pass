package xml.one.pass.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xml.one.pass.domain.model.PasswordModel
import xml.one.pass.domain.repository.PasswordRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val passwordRepository: PasswordRepository
) : ViewModel() {

    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.HomeState)
    val homeUiState: StateFlow<HomeUiState> = _homeUiState

    init {
        setUpHomePage()
    }

    private fun setUpHomePage() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val passwords = passwordRepository.loadPassword()

                withContext(Dispatchers.Main) {
                    _homeUiState.value = HomeUiState.PasswordStored(
                        passwordStored = passwords.size.toString()
                    )
                    _homeUiState.value = HomeUiState.PasswordCompromised(
                        passwordCompromised = "0"
                    )
                    _homeUiState.value = HomeUiState.Passwords(
                        passwords = passwords
                    )
                }
            }
        }
    }
}

sealed class HomeUiState {
    object HomeState : HomeUiState()

    data class PasswordStored(val passwordStored: String) : HomeUiState()

    data class PasswordCompromised(val passwordCompromised: String) : HomeUiState()

    data class Passwords(val passwords: List<PasswordModel>) : HomeUiState()
}
