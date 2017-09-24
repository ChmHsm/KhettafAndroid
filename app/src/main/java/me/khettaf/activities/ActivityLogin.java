package me.khettaf.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import ca.mimic.oauth2library.OAuth2Client;
import ca.mimic.oauth2library.OAuthResponse;
import me.khettaf.R;
import me.khettaf.activities.utils.AccessProperties;

import static me.khettaf.activities.utils.AccessProperties.getProperty;
import static me.khettaf.activities.utils.Connectivity.isNetworkAvailable;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
            progressDialog.hide();
            progressDialog.dismiss();
            if(message != null){
                if(message.getCode() != 200){
                    Snackbar
                            .make(findViewById(R.id.loginLayout), R.string.loginFailed, Snackbar.LENGTH_SHORT)
                            .show();
                }
                else{
                    //TODO store access and refresh tokens then continue to next activity
                    message.getExpiresAt();
                    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.access_token), message.getAccessToken());
                    editor.putString(getString(R.string.refresh_token), message.getRefreshToken());
                    editor.apply();
                }
            }
            else{
                Snackbar
                        .make(findViewById(R.id.loginLayout), R.string.loginFailed, Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
