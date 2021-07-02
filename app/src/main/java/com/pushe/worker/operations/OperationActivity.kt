package com.pushe.worker.operations

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavAction
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.pushe.worker.R
import com.pushe.worker.databinding.ActivityOperationBinding

import com.pushe.worker.ui.login.LoginActivity.USER_ID
import com.pushe.worker.ui.login.LoginActivity.USER_NAME

class OperationActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityOperationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId: String = intent.getStringExtra(USER_ID).toString()
        val userName: String = intent.getStringExtra(USER_NAME).toString()
        val args = Bundle()

        args.putString(USER_ID, userId)
        args.putString(USER_NAME, userName)

        binding = ActivityOperationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = userName

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_operation)
        navController.setGraph(navController.graph, args)

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_list -> {
                    navController.takeIf { it.currentDestination?.id == R.id.operation_total }
                        ?.navigate(R.id.action_Total_to_List, args)
                }
                R.id.action_total -> {
                    navController.takeIf { it.currentDestination?.id == R.id.operation_list }
                        ?.navigate(R.id.action_List_to_Total, args)
                }
            }
            true
        }

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_operation)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}

