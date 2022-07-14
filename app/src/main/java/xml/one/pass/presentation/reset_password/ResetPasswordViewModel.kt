package xml.one.pass.presentation.reset_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xml.one.pass.domain.repository.AccountRepository
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _resetUiState = MutableStateFlow<ResetUiState>(
        ResetUiState.Loading()
    )
    val resetUiState = _resetUiState.asStateFlow()

    fun resetPassword(password: String) {
        viewModelScope.launch {
            _resetUiState.value = ResetUiState.Loading(isLoading = true)
            withContext(Dispatchers.IO) {
                val account = accountRepository.loadAccount()
                val updatedPassword = accountRepository.updateAccountPassword(
                    password = password,
                    id = account.id
                )

                withContext(Dispatchers.Main) {
                    _resetUiState.value = ResetUiState.Loading(isLoading = false)
                    _resetUiState.value = if (updatedPassword == 1) {
                        ResetUiState.Success
                    } else {
                        ResetUiState.Error(message = "Unable to reset the password")
                    }
                }
            }
        }
    }
}

sealed class ResetUiState {
    data class Loading(val isLoading: Boolean = false) : ResetUiState()

    data class Error(val message: String) : ResetUiState()

    object Success : ResetUiState()
}
