package xml.one.pass

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import xml.one.pass.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_OnePass)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding?.let {
            setContentView(it.root)
        }

        setUpBottomNavController()
    }

    private fun setUpBottomNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navHostFragment.navController.apply {
            addOnDestinationChangedListener { _, _, args ->
                binding?.let {
                    it.bottomNavComponent.root.isVisible =
                        args?.getBoolean("hasBottomNav", false) == true
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
