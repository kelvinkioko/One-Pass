package xml.one.pass.presentation.password

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import xml.one.pass.R
import xml.one.pass.databinding.AddPasswordFragmentBinding
import xml.one.pass.extension.viewBinding

@AndroidEntryPoint
class AddPasswordFragment : Fragment(R.layout.add_password_fragment) {

    private val binding by viewBinding(AddPasswordFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
