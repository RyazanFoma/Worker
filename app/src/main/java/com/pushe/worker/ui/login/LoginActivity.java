package com.pushe.worker.ui.login;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.pushe.worker.R;
import com.pushe.worker.data.Result;
import com.pushe.worker.preference.Settings;

public class LoginActivity extends AppCompatActivity implements Observer<LoginState> {

    private StateViewModel stateViewModel;
    private LoginViewModel loginViewModel;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button logoutButton;
    private ProgressBar loadingProgressBar;
    private ImageButton scannerButton;
    private TextView barcodeText;

    private final TextWatcher afterTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // ignore
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // ignore
        }

        @Override
        public void afterTextChanged(Editable s) {
            loginViewModel.loginPasswordChanged(passwordEditText.getText().toString());
        }
    };

    private final Observer<LoginFormState> loginFormStateObserver = new Observer<LoginFormState>() {
        @Override
        public void onChanged(@Nullable LoginFormState loginFormState) {
            if (loginFormState == null) {
                return;
            }
            if (loginFormState.isDataValid()) {
                loginButton.setEnabled(true);
                return;
            }
            if (loginFormState.getRestApiError() != null) {
                Toast.makeText(getApplicationContext(), loginFormState.getRestApiError(),
                        Toast.LENGTH_LONG).show();
                stateViewModel.setStateToBarCode();
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        }
    };

    private final Observer<Result<?>> resultObserver = new Observer<Result<?>>() {
        @Override
        public void onChanged(Result<?> result) {
            loginViewModel.loginSourceChanged(result);
            loadingProgressBar.setVisibility(View.GONE);
            if (result instanceof Result.Success) {
                usernameEditText.setText(loginViewModel.loggedInUserView.getDisplayName());
                passwordEditText.setText("");
                stateViewModel.setStateToPassword();
            }
        }
    };

    private final View.OnClickListener loginOnClickListener = new View.OnClickListener() {
        @SuppressLint("ShowToast")
        @Override
        public void onClick(View v) {
            if (loginViewModel.isVerifiedPassword(passwordEditText.getText().toString())) {
                Toast.makeText(getApplicationContext(), R.string.welcome, Toast.LENGTH_LONG).show();
                //Здесь вызываем следующую активность, а пока гудбай.
//                loginViewModel.loggedInUserView.getUserId();
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(), R.string.login_failed, Toast.LENGTH_LONG).show();
                passwordEditText.setText("");
                loginButton.setEnabled(false);
            }
        }
    };

    private final View.OnClickListener logoutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            stateViewModel.setStateToBarCode();
        }
    };

    public final int CUSTOMIZED_REQUEST_CODE = 0x0000ffff;

    private final View.OnClickListener scannerOnClickListener = view -> new IntentIntegrator(LoginActivity.this)
            .setPrompt("Штрих код сотрудника")
            .setRequestCode(CUSTOMIZED_REQUEST_CODE)
            .setTimeout(60000)
            .initiateScan();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        logoutButton = findViewById(R.id.logout);
        loadingProgressBar = findViewById(R.id.loading);
        scannerButton = findViewById(R.id.barcodeButton);
        barcodeText = findViewById(R.id.barcodeText);

        stateViewModel = new ViewModelProvider(this).get(StateViewModel.class);
        stateViewModel.getMutableLiveData().observe(this, this);

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(this))
                .get(LoginViewModel.class);
        loginViewModel.getLoginFormState().observe(this, loginFormStateObserver);
        loginViewModel.getLoginDataSource().observe(this, resultObserver);

        passwordEditText.addTextChangedListener(afterTextChangedListener);

        loginButton.setOnClickListener(loginOnClickListener);
        logoutButton.setOnClickListener(logoutOnClickListener);
        scannerButton.setOnClickListener(scannerOnClickListener);
    }

    /**
     * Callback for barcode scanner
     * @param requestCode - see {@link #CUSTOMIZED_REQUEST_CODE}
     * @param resultCode - ignore
     * @param data - contains barcode
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != CUSTOMIZED_REQUEST_CODE && requestCode != IntentIntegrator.REQUEST_CODE) {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

        if(result.getContents() == null) {
            Intent originalIntent = result.getOriginalIntent();
            if (originalIntent == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else if(originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                Toast.makeText(this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("MainActivity", "Scanned");
            Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            loadingProgressBar.setVisibility(View.VISIBLE);
            loginViewModel.loginBarcodeChanged(result.getContents());
        }
    }

    /**
     * Create menu
     * @param menu - menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    /**
     * Callback for selecting setting
     * @param item - setting item
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            startActivity(new Intent(this, Settings.class));
            stateViewModel.setStateToBarCode();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * ViewModel observer for managing the state of the form
     * @param loginState - Mutable
     */
    @Override
    public void onChanged(LoginState loginState) {
        switch (loginState.getActivityState()) {
            case PASSWORD:
                scannerButton.setVisibility(View.GONE);
                barcodeText.setVisibility(View.GONE);
                usernameEditText.setVisibility(View.VISIBLE);
                passwordEditText.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.VISIBLE);
                loginButton.setEnabled(false);
                break;
            case BARCODE:
            default:
                scannerButton.setVisibility(View.VISIBLE);
                barcodeText.setVisibility(View.VISIBLE);
                usernameEditText.setVisibility(View.GONE);
                passwordEditText.setVisibility(View.GONE);
                logoutButton.setVisibility(View.GONE);
                loginButton.setVisibility(View.GONE);
                loginButton.setEnabled(false);
        }
    }
}