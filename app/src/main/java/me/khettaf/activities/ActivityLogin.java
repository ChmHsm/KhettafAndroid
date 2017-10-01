package me.khettaf.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.io.IOException;
import java.util.List;

import ca.mimic.oauth2library.OAuth2Client;
import ca.mimic.oauth2library.OAuthResponse;
import me.khettaf.R;
import me.khettaf.database.AppDatabase;
import me.khettaf.entities.Khettaf;
import me.khettaf.entities.POI;
import me.khettaf.entities.Trajet;
import me.khettaf.retrofittedWS.ServiceGenerator;
import me.khettaf.retrofittedWS.TrajetsInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static me.khettaf.activities.utils.AccessProperties.getProperty;
import static me.khettaf.activities.utils.Authentication.isAccessTokenAvailable;
import static me.khettaf.activities.utils.Authentication.isAuthenticationRequired;
import static me.khettaf.activities.utils.Authentication.isRefreshTokenAvailable;
import static me.khettaf.activities.utils.Connectivity.isNetworkAvailable;
import static me.khettaf.activities.utils.InputHandling.hideKeyboard;

public class ActivityLogin extends AppCompatActivity {

    private Button loginButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.loginButton);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable(ActivityLogin.this)){
                    new SendCredentialsAndGetAccessToken().execute();
                }
                else{
                    Snackbar
                            .make(findViewById(R.id.loginLayout), R.string.networkUnavailable, Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });

        if(!isAuthenticationRequired(ActivityLogin.this)){
            Intent intent = new Intent(ActivityLogin.this, TrajetsMainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            if(isRefreshTokenAvailable(ActivityLogin.this)){

                SharedPreferences authPrefs = getSharedPreferences(
                        getString(R.string.authentication_prefs), Context.MODE_PRIVATE);

                String username = authPrefs.getString(
                        getString(R.string.last_username), "");
                String password = authPrefs.getString(
                        getString(R.string.last_password), "");

                if(!username.equals("") && !password.equals("")){
                    usernameEditText.setText(username);
                    passwordEditText.setText(password);
                    new SendCredentialsAndGetAccessToken().execute();
                }

            }
        }

        hideKeyboard(ActivityLogin.this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard(ActivityLogin.this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hideKeyboard(ActivityLogin.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private class SendCredentialsAndGetAccessToken extends AsyncTask<Void, OAuthResponse, OAuthResponse> {

        private String username;
        private String password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            username = usernameEditText.getText().toString().trim();
            password = passwordEditText.getText().toString().trim();
            progressDialog = new ProgressDialog(ActivityLogin.this);
            progressDialog.setMessage(getResources().getString(R.string.loggingIn));
            progressDialog.show();
        }

        @Override
        protected OAuthResponse doInBackground(Void... params) {

            OAuthResponse response;
            OAuth2Client.Builder builder = new OAuth2Client.Builder(
                    getProperty(getApplicationContext(), "ws_creds", "app_name"),
                    getProperty(getApplicationContext(), "ws_creds", "password"),
                    getProperty(getApplicationContext(), "ws_creds", "token_url"))
                    .grantType(getProperty(getApplicationContext(), "ws_creds", "grant_type"))
                    .scope(getProperty(getApplicationContext(), "ws_creds", "scope"))
                    .username(username)
                    .password(password);

            try {
                 response = builder.build().requestAccessToken();
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(OAuthResponse message) {
            super.onPostExecute(message);

            if(message != null){
                if(message.getCode() != 200){
                    progressDialog.hide();
                    progressDialog.dismiss();
                    Snackbar
                            .make(findViewById(R.id.loginLayout), R.string.loginFailed, Snackbar.LENGTH_SHORT)
                            .show();
                }
                else{
                    SharedPreferences sharedPref = getSharedPreferences(
                            getString(R.string.authentication_prefs), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.access_token), message.getAccessToken());
                    editor.putString(getString(R.string.refresh_token), message.getRefreshToken());
                    editor.putLong(getString(R.string.token_expires_at), message.getExpiresAt());
                    editor.putString(getString(R.string.last_username), username);
                    editor.putString(getString(R.string.last_password), password);
                    editor.apply();

                    Intent intent = new Intent(ActivityLogin.this, TrajetsMainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            else{
                progressDialog.hide();
                progressDialog.dismiss();
                Snackbar
                        .make(findViewById(R.id.loginLayout), R.string.loginFailed, Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
