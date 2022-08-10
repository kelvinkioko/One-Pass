@file:OptIn(ExperimentalCoroutinesApi::class)

package xml.one.pass.presentation.login

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import xml.one.pass.MainDispatcherRule
import xml.one.pass.R
import xml.one.pass.data.preference.OnePassRepositoryTestImpl
import xml.one.pass.data.repository.AccountRepositoryTestImpl
import xml.one.pass.util.TextResource

class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel
    private val stateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Loading())
    private fun getStateFlow(): StateFlow<LoginUiState> = stateFlow.asStateFlow()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(
            accountRepository = AccountRepositoryTestImpl(),
            onePassRepository = OnePassRepositoryTestImpl()
        )
    }

    @Test
    fun `attempt to login with empty email and password`() = runTest {
        loginViewModel.login(email = "", password = "")
    }

    @Test
    fun `attempt to login with valid email and password`() = runTest {
        loginViewModel.login(email = "kiokokelvin@gmail.com", password = "12345678")

        val response = launch { loginViewModel.loginUiState.collect() }

        assertThat(response).isEqualTo(LoginUiState.Success)

        response.cancel()
    }

    @Test
    fun `attempt to login with invalid email and password`() {
        loginViewModel.login(email = "kiokokelvin@gmail.com", password = "12345678")
    }

    @Test
    fun `attempt to login with invalid email`() {
        loginViewModel.login(email = "kiokokelvin@gmail.com", password = "12345678")
    }

    @Test
    fun `attempt to login with invalid password`() {
        loginViewModel.login(email = "kiokokelvin@gmail.com", password = "12345678")
    }
}