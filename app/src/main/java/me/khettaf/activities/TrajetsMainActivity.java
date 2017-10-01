package me.khettaf.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.mimic.oauth2library.OAuth2Client;
import ca.mimic.oauth2library.OAuthResponse;
import me.khettaf.R;
import me.khettaf.adapters.TrajetsAdapter;

import me.khettaf.database.AppDatabase;
import me.khettaf.entities.Trajet;
import me.khettaf.retrofittedWS.ServiceGenerator;
import me.khettaf.retrofittedWS.TrajetsInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static me.khettaf.activities.utils.AccessProperties.getProperty;
import static me.khettaf.activities.utils.Authentication.isAuthenticationRequired;
import static me.khettaf.activities.utils.Authentication.isRefreshTokenAvailable;

/**
 * Created by Me on 27/09/2017.
 */

public class TrajetsMainActivity extends AppCompatActivity {

    private ArrayList<Trajet> trajets = new ArrayList<>();
    private TrajetsAdapter trajetsAdapter;
    private RecyclerView trajetsRecyclerView;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    private String accessToken;
    private String username;
    private String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_trajets);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        trajetsRecyclerView = (RecyclerView) findViewById(R.id.trajetRecyclerView);
        trajetsAdapter = new TrajetsAdapter(trajets);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TrajetsMainActivity.this);
        trajetsRecyclerView.setLayoutManager(layoutManager);
        trajetsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        trajetsRecyclerView.setAdapter(trajetsAdapter);

        retrieveAllLocalTrajets();

        SharedPreferences authPrefs = getSharedPreferences(
                getString(R.string.authentication_prefs), Context.MODE_PRIVATE);

        accessToken = authPrefs.getString(
                getString(R.string.access_token), "");

        username = authPrefs.getString(
                getString(R.string.last_username), "");
        password = authPrefs.getString(
                getString(R.string.last_password), "");

        if (!isAuthenticationRequired(TrajetsMainActivity.this)) {

            new RetrieveMoreTrajets().execute();
        } else {

            if (!username.equals("") && !password.equals("")) {
                new SendCredentialsAndGetAccessToken().execute();

            }
        }

    }

    private void retrieveAllLocalTrajets() {

        progressDialog = new ProgressDialog(TrajetsMainActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loggingIn));
        progressDialog.show();

        SQLite.select()
                .from(Trajet.class)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<Trajet>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Trajet> tResult) {
                        if(tResult.size() > 0){
                            trajets.clear();
                        }
                        for (Trajet trajet : tResult
                                ) {
                            trajets.add(trajet);
                        }
                        trajetsAdapter.notifyDataSetChanged();
                        progressDialog.hide();
                        progressDialog.dismiss();
                    }

                }).execute();

    }

    private class RetrieveMoreTrajets extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {


            TrajetsInterface api = ServiceGenerator.createService(TrajetsInterface.class);

            Call<List<Trajet>> call = api.getAllTrajets(accessToken);
            call.enqueue(new Callback<List<Trajet>>() {
                @Override
                public void onResponse(Call<List<Trajet>> call, Response<List<Trajet>> response) {

                    if (response.body() != null) {
                        final List<Trajet> trajets = response.body();
                        DatabaseDefinition database = FlowManager.getDatabase(AppDatabase.class);
                        Transaction transaction = database.beginTransactionAsync(new ITransaction() {
                            @Override
                            public void execute(DatabaseWrapper databaseWrapper) {

                                for (Trajet trajet : trajets
                                        ) {
                                    trajet.getDepart().save();
                                    trajet.getDestination().save();
                                    trajet.getKhettaf().save();
                                    trajet.save();
                                }

                            }
                        }).build();
                        transaction.execute();
                        retrieveAllLocalTrajets();
                    }
                    else{
                        new SendCredentialsAndGetAccessToken().execute();

                    }

                }

                @Override
                public void onFailure(Call<List<Trajet>> call, Throwable t) {


                    Snackbar
                            .make(findViewById(R.id.trajetRecyclerView), R.string.loginFailed, Snackbar.LENGTH_SHORT)
                            .show();
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    private class SendCredentialsAndGetAccessToken extends AsyncTask<Void, OAuthResponse, OAuthResponse> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

            if (message != null) {
                if (message.getCode() != 200) {
                    Snackbar
                            .make(findViewById(R.id.trajetRecyclerView), R.string.loginFailed, Snackbar.LENGTH_SHORT)
                            .show();
                } else {
                    accessToken = message.getAccessToken();
                    SharedPreferences sharedPref = getSharedPreferences(
                            getString(R.string.authentication_prefs), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.access_token), message.getAccessToken());
                    editor.putString(getString(R.string.refresh_token), message.getRefreshToken());
                    editor.putLong(getString(R.string.token_expires_at), message.getExpiresAt());
                    editor.putString(getString(R.string.last_username), username);
                    editor.putString(getString(R.string.last_password), password);
                    editor.apply();

                    new RetrieveMoreTrajets().execute();
                }
            } else {
                Snackbar
                        .make(findViewById(R.id.trajetRecyclerView), R.string.loginFailed, Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
