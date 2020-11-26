package com.andariadar.newsapplication.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.andariadar.newsapplication.R
import com.andariadar.newsapplication.ui.fragments.FullNewsFragment
import com.andariadar.newsapplication.ui.fragments.FullNewsFragmentArgs
import com.andariadar.newsapplication.ui.fragments.NewsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            as NavHostFragment
        navController = navHostFragment.findNavController()

        val extras = intent.extras
        if(extras != null) {
            val value = extras.get("key")
            val url = extras.get("url")
            if (value == true) {

                val bundle = bundleOf("url" to url)
                navController.navigate(R.id.action_newsFragment_to_fullNewsFragment, bundle)
            }
        }

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}