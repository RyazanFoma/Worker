package com.pushe.worker.operations

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.pushe.worker.R
import com.pushe.worker.databinding.ActivityOperationBinding

import com.pushe.worker.ui.login.LoginActivity.USER_ID
import com.pushe.worker.ui.login.LoginActivity.USER_NAME
import androidx.navigation.NavController
import com.pushe.worker.NavGraphDirections
import com.pushe.worker.totals.TotalsFragmentDirections

class OperationActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityOperationBinding
    private lateinit var navController: NavController
    private var args = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId: String = intent.getStringExtra(USER_ID).toString()

        args.putString(USER_ID, userId)

        binding = ActivityOperationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = intent.getStringExtra(USER_NAME).toString()

        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_graph, args)

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        This line is commented out because it does not pass arguments to the fragment
//        binding.include.bottomNav.setupWithNavController(navController)
//        therefore used a listener
        binding.include.bottomNav.setOnNavigationItemSelectedListener { item ->
            super.onOptionsItemSelected(item)
            when (item.itemId) {
                R.id.operation_list -> {
                    navController.takeIf { it.currentDestination?.id == R.id.operation_total }
                        ?.navigate(TotalsFragmentDirections.actionTotalToList(userId = userId))
                }
                R.id.operation_total -> {
                    navController.takeIf { it.currentDestination?.id == R.id.operation_list }
                        ?.navigate(OperationsFragmentDirections.actionListToTotal(userId = userId))
                }
            }
            true
        }

        //Call barcode scanner to operation after clicking on fab button
        binding.fab.setOnClickListener { _ ->
            navController.navigate(NavGraphDirections.actionGoToScanner(userId = userId))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}

