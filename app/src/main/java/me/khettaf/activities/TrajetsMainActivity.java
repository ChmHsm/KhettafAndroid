package me.khettaf.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;
import java.util.List;

import me.khettaf.R;
import me.khettaf.adapters.TrajetsAdapter;

import me.khettaf.entities.Trajet;

/**
 * Created by Me on 27/09/2017.
 */

public class TrajetsMainActivity extends AppCompatActivity {

    private ArrayList<Trajet> trajets= new ArrayList<>();
    private TrajetsAdapter trajetsAdapter;
    private RecyclerView trajetsRecyclerView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_trajets);

        trajetsRecyclerView = (RecyclerView) findViewById(R.id.trajetRecyclerView);
        trajetsAdapter = new TrajetsAdapter(trajets);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TrajetsMainActivity.this);
        trajetsRecyclerView.setLayoutManager(layoutManager);
        trajetsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        trajetsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        trajetsRecyclerView.setAdapter(trajetsAdapter);

        retrieveTrajets();

    }

    private void retrieveTrajets(){

        progressDialog = new ProgressDialog(TrajetsMainActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loggingIn));
        progressDialog.show();

        SQLite.select()
                .from(Trajet.class)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<Trajet>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Trajet> tResult) {
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
}
