package xml.one.pass.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import xml.one.pass.R
import xml.one.pass.databinding.HomeFragmentBinding
import xml.one.pass.extension.viewBinding

class HomeFragment : Fragment(R.layout.home_fragment) {

    private val binding by viewBinding(HomeFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpClickListener()
    }

    private fun setUpClickListener() {
        binding.apply {
            searchInput.setEndIconOnClickListener {
                searchInput.editText?.setText("")
            }
        }
    }
}