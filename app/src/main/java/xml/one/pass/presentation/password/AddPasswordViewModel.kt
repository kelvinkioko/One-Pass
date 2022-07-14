package xml.one.pass.presentation.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xml.one.pass.domain.repository.PasswordRepository
import xml.one.pass.extension.getCurrentDate
import xml.one.pass.util.Resource
import javax.inject.Inject

@HiltViewModel
class AddPasswordViewModel @Inject constructor(
    private val passwordRepository: PasswordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddPasswordUiState>(AddPasswordUiState.Loading())
    val uiState = _uiState.asStateFlow()

    fun savePassword(
        siteName: String = "",
        url: String = "",
        userName: String = "",
        email: String = "",
        password: String = "",
        phoneNumber: String = ""
    ) {
        viewModelScope.launch {
            _uiState.value = AddPasswordUiState.Loading(isLoading = true)
            val status = withContext(Dispatchers.IO) {
                passwordRepository.insertPassword(
                    siteName = siteName,
                    url = url,
                    userName = userName,
                    email = email,
                    password = password,
                    phoneNumber = phoneNumber,
                    securityQuestions = "",
                    timeCreated = getCurrentDate(),
                    timeUpdated = getCurrentDate()
                )
            }

            _uiState.value = AddPasswordUiState.Loading(isLoading = false)

            status.onEach { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _uiState.value = AddPasswordUiState.Error(
                            errorMessage = resource.message ?: ""
                        )
                    }
                    is Resource.Success -> {
                        _uiState.value = if (resource.data == true)
                            AddPasswordUiState.Success
                        else
                            AddPasswordUiState.Error(errorMessage = "An error occurred while saving your password details sealed")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}

sealed class AddPasswordUiState {
    data class Loading(val isLoading: Boolean = false) : AddPasswordUiState()

    object Success : AddPasswordUiState()

    data class Error(val errorMessage: String) : AddPasswordUiState()
}
