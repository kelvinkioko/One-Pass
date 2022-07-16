package xml.one.pass.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xml.one.pass.R
import xml.one.pass.domain.repository.AccountRepository
import xml.one.pass.util.ConstantsUtil.DELAY_TIME
import xml.one.pass.util.TextResource
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading())
    val loginUiState = _loginUiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState.Loading(isLoading = true)
            withContext(Dispatchers.IO) {
                val accountExists = accountRepository.doesAccountExistWithEmailAndPassword(
                    email = email,
                    password = password
                )
                delay(DELAY_TIME)
                withContext(Dispatchers.Main) {
                    _loginUiState.value = LoginUiState.Loading(isLoading = false)
                    _loginUiState.value = if (accountExists) {
                        LoginUiState.Success
                    } else {
                        LoginUiState.Error(
                            message = TextResource.StringResource(R.string.login_credentials_error)
                        )
                    }
                }
            }
        }
    }
}

sealed class LoginUiState {
    data class Loading(val isLoading: Boolean = false) : LoginUiState()

    data class Error(val message: TextResource) : LoginUiState()

    object Success : LoginUiState()
}
