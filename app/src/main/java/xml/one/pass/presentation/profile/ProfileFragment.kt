package xml.one.pass.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import xml.one.pass.R
import xml.one.pass.databinding.ProfileFragmentBinding
import xml.one.pass.extension.viewBinding

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.profile_fragment) {

    private val binding by viewBinding(ProfileFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
