package xml.one.pass.common

import androidx.navigation.NavDirections

sealed class GlobalAction {
    data class Navigate(val directions: NavDirections) : GlobalAction()
}
