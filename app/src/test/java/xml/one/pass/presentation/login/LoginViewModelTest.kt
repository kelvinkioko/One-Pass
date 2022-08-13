@file:OptIn(ExperimentalCoroutinesApi::class)

package xml.one.pass.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import xml.one.pass.MainDispatcherRule
import xml.one.pass.data.preference.OnePassRepositoryTestImpl
import xml.one.pass.data.repository.AccountRepositoryTestImpl

class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

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

        val value = loginViewModel.loginUiState

        assertThat(value).isEqualTo(LoginUiState.Success)
    }

    @Test
    fun `attempt to login with valid email and password`() = runTest {
        loginViewModel.login(email = "kiokokelvin@gmail.com", password = "12345678")

        val value = loginViewModel.loginUiState

        assertThat(value).isEqualTo(LoginUiState.Success)
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
