package me.khettaf.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.khettaf.R;
import me.khettaf.adapters.holders.TrajetViewHolder;
import me.khettaf.entities.Trajet;

import static me.khettaf.utils.DataManaging.translateDate;

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
        holder.titreTextView.setText(trajet.getDestination().getNom());
        holder.departTextView.setText(trajet.getDepart().getNom());
        holder.destinationTextView.setText(trajet.getDestination().getNom());
        holder.descriptionTextView.setText(trajet.getDescription());
        holder.dateTextView.setText(translateDate(trajet.getDateDepart()));
    }

    public TrajetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View trajetHolderView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_trajet, parent, false);

        return new TrajetViewHolder(trajetHolderView);
    }

    @Override
    public int getItemCount() {
        return trajets == null ? 0 : trajets.size();
    }
}
