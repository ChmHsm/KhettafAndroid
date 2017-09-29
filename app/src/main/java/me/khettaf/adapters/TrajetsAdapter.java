package me.khettaf.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.khettaf.R;
import me.khettaf.adapters.holders.TrajetViewHolder;
import me.khettaf.entities.Trajet;

/**
 * Created by Me on 28/09/2017.
 */

public class TrajetsAdapter extends RecyclerView.Adapter<TrajetViewHolder>{

    private ArrayList<Trajet> trajets = new ArrayList<>();

    public TrajetsAdapter(ArrayList<Trajet> trajets){
        this.trajets = trajets;
    }

    @Override
    public void onBindViewHolder(TrajetViewHolder holder, int position) {
        Trajet trajet = trajets.get(position);
        holder.setTrajet(trajet);
        trajet.getDepart().load();
        holder.departTextView.setText(trajet.getDepart().getNom());
    }

    public TrajetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View trajetHholderView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_trajet, parent, false);

        return new TrajetViewHolder(trajetHholderView);
    }

    @Override
    public int getItemCount() {
        return trajets == null ? 0 : trajets.size();
    }
}
