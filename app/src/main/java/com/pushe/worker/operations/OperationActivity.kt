package com.pushe.worker.operations

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import com.pushe.worker.R
import com.pushe.worker.databinding.ActivityOperationBinding

import com.pushe.worker.ui.login.LoginActivity.USER_ID
import com.pushe.worker.ui.login.LoginActivity.USER_NAME
import android.R.attr.data
import android.util.Log

import android.widget.Toast
import androidx.navigation.NavController
import com.google.zxing.client.android.Intents

import com.google.zxing.integration.android.IntentResult
import com.pushe.worker.NavGraphDirections

class OperationActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityOperationBinding
    private lateinit var navController: NavController
    private var args = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId: String = intent.getStringExtra(USER_ID).toString()
        val userName: String = intent.getStringExtra(USER_NAME).toString()

        args.putString(USER_ID, userId)

        binding = ActivityOperationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = userName

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
                        ?.navigate(SecondFragmentDirections.actionTotalToList(userId = userId))
                }
                R.id.operation_total -> {
                    navController.takeIf { it.currentDestination?.id == R.id.operation_list }
                        ?.navigate(OperationFragmentDirections.actionListToTotal(userId = userId))
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

    companion object{
        const val BARCODE = "barcode"
    }
}

