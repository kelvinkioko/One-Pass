package xml.one.pass.presentation.login

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xml.one.pass.R
import xml.one.pass.launchFragmentInHiltContainer
import xml.one.pass.presentation.welcome.WelcomeFragmentDirections

@MediumTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    lateinit var navController: NavController

    @Before
    fun setup() {
        hiltRule.inject()
        navController = TestNavHostController(ApplicationProvider.getApplicationContext()).also {
            runOnUiThread {
                it.setGraph(R.navigation.app_nav_graph)
                it.navigate(WelcomeFragmentDirections.toLoginFragment())
            }
        }
    }

    @Test
    fun fragmentLaunchesSuccessfully() {
        launchFragmentInHiltContainer<LoginFragment>(themeResId = R.style.Theme_OnePass)
    }

    @Test
    fun clickForgotPassword() {
        launchFragmentInHiltContainer<LoginFragment>(themeResId = R.style.Theme_OnePass) {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.signInForgot)).perform(click())

        assertThat(navController.currentDestination?.id).isEqualTo(R.id.forgotPasswordFragment)
    }

    @Test
    fun clickSignUp() {
        launchFragmentInHiltContainer<LoginFragment>(themeResId = R.style.Theme_OnePass) {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.signUpAction)).perform(click())

        assertThat(navController.currentDestination?.id).isEqualTo(R.id.registerFragment)
    }
}
