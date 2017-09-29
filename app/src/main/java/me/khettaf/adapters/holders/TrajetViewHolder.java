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

    public TrajetViewHolder(View view){
        super(view);
        departTextView = (TextView) view.findViewById(R.id.departTextView);
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }
}
