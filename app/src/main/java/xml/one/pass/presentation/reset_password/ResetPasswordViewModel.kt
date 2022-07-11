package xml.one.pass.presentation.reset_password

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
class ResetPasswordViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _resetPasswordUiState = MutableStateFlow<ResetPasswordUiState>(
        ResetPasswordUiState.Loading(isLoading = false)
    )
    val resetPasswordUiState: StateFlow<ResetPasswordUiState> = _resetPasswordUiState

    fun resetPassword(password: String) {
        viewModelScope.launch {
            _resetPasswordUiState.value = ResetPasswordUiState.Loading(isLoading = true)
            withContext(Dispatchers.IO) {
                val account = accountRepository.loadAccount()
                val updatedPassword = accountRepository.updateAccountPassword(
                    password = password,
                    id = account.id
                )

                withContext(Dispatchers.Main) {
                    _resetPasswordUiState.value = ResetPasswordUiState.Loading(isLoading = false)
                    _resetPasswordUiState.value = if (updatedPassword == 1) {
                        ResetPasswordUiState.Success
                    } else {
                        ResetPasswordUiState.Error(message = "Unable to reset the password")
                    }
                }
            }
        }
    }
}

sealed class ResetPasswordUiState {
    data class Loading(val isLoading: Boolean) : ResetPasswordUiState()

    data class Error(val message: String) : ResetPasswordUiState()

    object Success : ResetPasswordUiState()
}
