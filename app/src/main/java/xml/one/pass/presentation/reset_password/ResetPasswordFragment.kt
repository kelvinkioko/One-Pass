package xml.one.pass.presentation.reset_password

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import xml.one.pass.R
import xml.one.pass.databinding.ResetPasswordFragmentBinding
import xml.one.pass.extension.viewBinding

class ResetPasswordFragment : Fragment(R.layout.reset_password_fragment) {

    private val binding by viewBinding(ResetPasswordFragmentBinding::bind)

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
            resetAction.setOnClickListener {}
        }
    }
}
