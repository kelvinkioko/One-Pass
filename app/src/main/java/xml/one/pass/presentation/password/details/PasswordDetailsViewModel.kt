package xml.one.pass.presentation.password.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xml.one.pass.domain.model.PasswordModel
import xml.one.pass.domain.repository.PasswordRepository
import xml.one.pass.util.Resource
import xml.one.pass.util.TextResource
import javax.inject.Inject

@HiltViewModel
class PasswordDetailsViewModel @Inject constructor(
    private val passwordRepository: PasswordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PasswordDetailsUiState>(PasswordDetailsUiState.DefaultState)
    var uiState = _uiState.asStateFlow()

    private var passwordID: Int = 0

    fun loadPasswordDetails(passwordId: Int) {
        this.passwordID = passwordId
        viewModelScope.launch {
            val password = withContext(Dispatchers.IO) {
                passwordRepository.loadPasswordById(passwordId = passwordId)
            }

            password.onEach { resource ->
                when (resource) {
                    is Resource.Error ->
                        _uiState.value = PasswordDetailsUiState.Error(
                            errorMessage = TextResource.DynamicString(resource.message ?: "")
                        )
                    is Resource.Success -> {
                        resource.data?.let { passwordModel ->
                            _uiState.value = PasswordDetailsUiState.PasswordDetails(
                                password = passwordModel
                            )
                        } ?: kotlin.run {
                            _uiState.value = PasswordDetailsUiState.Error(
                                errorMessage = TextResource.DynamicString(resource.message ?: "")
                            )
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun navigateToEditPasswordDetails() {
        val direction = PasswordDetailsFragmentDirections.toAddPasswordFragment(passwordID)
        _uiState.value = PasswordDetailsUiState.EditPasswordDetails(detailsDestination = direction)
    }
}

sealed class PasswordDetailsUiState {
    object DefaultState : PasswordDetailsUiState()

    data class PasswordDetails(
        val password: PasswordModel
    ) : PasswordDetailsUiState()

    data class Error(
        val errorMessage: TextResource
    ) : PasswordDetailsUiState()

    // Navigation
    data class EditPasswordDetails(
        val detailsDestination: NavDirections
    ) : PasswordDetailsUiState()
}
