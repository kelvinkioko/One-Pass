package xml.one.pass.presentation.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import xml.one.pass.R
import xml.one.pass.databinding.RegisterFragmentBinding
import xml.one.pass.extension.viewBinding

class RegisterFragment : Fragment(R.layout.register_fragment) {

    private val binding by viewBinding(RegisterFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpOnClickListener()
    }

    private fun setUpOnClickListener() {
        binding.apply {
            registerAction.setOnClickListener {
                findNavController().navigate(RegisterFragmentDirections.toHomeFragment())
            }

            signInAction.setOnClickListener {
                findNavController().navigate(RegisterFragmentDirections.toLoginFragment())
            }
        }
    }
}
