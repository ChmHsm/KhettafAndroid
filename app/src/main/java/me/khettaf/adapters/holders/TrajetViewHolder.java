package me.khettaf.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.khettaf.R;
import me.khettaf.entities.Trajet;

/**
 * Created by Me on 28/09/2017.
 */

public class TrajetViewHolder extends RecyclerView.ViewHolder{

    private Trajet trajet;
    public TextView departTextView;
    public TextView destinationTextView;
    public TextView titreTextView;
    public TextView descriptionTextView;
    public TextView dateTextView;

    public TrajetViewHolder(View view){
        super(view);
        titreTextView = (TextView) view.findViewById(R.id.titreTextView);
        departTextView = (TextView) view.findViewById(R.id.departTextView);
        destinationTextView = (TextView) view.findViewById(R.id.destinationTextView);
        descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
        dateTextView = (TextView) view.findViewById(R.id.dateTextView);
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }
}
