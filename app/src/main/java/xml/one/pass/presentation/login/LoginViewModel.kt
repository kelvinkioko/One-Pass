package xml.one.pass.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xml.one.pass.domain.repository.AccountRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _loginUiState = MutableStateFlow<LoginUiState>(
        LoginUiState.Loading(isLoading = false)
    )
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState.Loading(isLoading = true)
            withContext(Dispatchers.IO) {
                val accountExists = accountRepository.doesAccountExistWithEmailAndPassword(
                    email = email,
                    password = password
                )
                withContext(Dispatchers.Main) {
                    _loginUiState.value = LoginUiState.Loading(isLoading = false)
                    _loginUiState.value = if (accountExists) {
                        LoginUiState.Success
                    } else {
                        LoginUiState.Error(
                            message = "Login credentials are invalid. Please try again"
                        )
                    }
                }
            }
        }
    }
}

sealed class LoginUiState {
    data class Loading(val isLoading: Boolean) : LoginUiState()

    data class Error(val message: String) : LoginUiState()

    object Success : LoginUiState()
}
