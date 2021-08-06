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
        args.putString(USER_NAME, userName)

        binding = ActivityOperationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = userName

        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.setGraph(navController.graph, args)

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        This line is commented out because it does not pass arguments to the fragment
//        binding.bottomNav.setupWithNavController(navController)
//        therefore used a listener
        binding.include.bottomNav.setOnNavigationItemSelectedListener { item ->
            super.onOptionsItemSelected(item)
            when (item.itemId) {
                R.id.operation_list -> {
                    navController.takeIf { it.currentDestination?.id == R.id.operation_total }
                        ?.navigate(R.id.action_Total_to_List, args)
                }
                R.id.operation_total -> {
                    navController.takeIf { it.currentDestination?.id == R.id.operation_list }
                        ?.navigate(R.id.action_List_to_Total, args)
                }
            }
            true
        }

        //Call barcode scanner to operation
        binding.fab.setOnClickListener { view ->
            IntentIntegrator(this)
                .setPrompt("Штрих код операции")
//                .setRequestCode(IntentIntegrator.REQUEST_CODE)
                .setTimeout(60000)
                .initiateScan()
        }
    }

    /**
     * Callback for barcode scanner
     * @param requestCode - see {@link #IntentIntegrator.REQUEST_CODE}
     * @param resultCode - ignore
     * @param data - contains barcode
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result!=null) {
            if (result.contents == null) {
                val originalIntent = result.originalIntent
                if (originalIntent == null) {
                    Log.d("OperationActivity", "Cancelled scan")
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                    Log.d("OperationActivity", "Cancelled scan due to missing camera permission")
                    Toast.makeText(
                        this,
                        "Cancelled due to missing camera permission",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Log.d("OperationActivity", "Scanned")
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                /** Perform global action for operation information **/
                navController.takeIf { it.currentDestination?.id == R.id.operation_total }
                    ?.navigate(R.id.action_Total_to_List, args)
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}

