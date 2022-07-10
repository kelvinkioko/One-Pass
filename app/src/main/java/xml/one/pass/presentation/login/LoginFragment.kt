package xml.one.pass.presentation.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import xml.one.pass.R
import xml.one.pass.databinding.LoginFragmentBinding
import xml.one.pass.extension.viewBinding

class LoginFragment : Fragment(R.layout.login_fragment) {

    private val binding by viewBinding(LoginFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpOnClickListener()
    }

    private fun setUpOnClickListener() {
        binding.apply {
            signInForgot.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.toForgotPasswordFragment())
            }

            signInAction.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.toHomeFragment())
            }

            signUpAction.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.toRegisterFragment())
            }
        }
    }
}
