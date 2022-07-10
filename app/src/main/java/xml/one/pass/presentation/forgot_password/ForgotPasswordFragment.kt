package xml.one.pass.presentation.forgot_password

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import xml.one.pass.R
import xml.one.pass.databinding.ForgotPasswordFragmentBinding
import xml.one.pass.extension.viewBinding

class ForgotPasswordFragment : Fragment(R.layout.forgot_password_fragment) {

    private val binding by viewBinding(ForgotPasswordFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setUpOnClickListener()
    }

    private fun setupToolbar() {
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun setUpOnClickListener() {
        binding.apply {
            submitAction.setOnClickListener {
                findNavController().navigate(
                    ForgotPasswordFragmentDirections.toResetPasswordFragment()
                )
            }
        }
    }
}
