package me.khettaf.retrofittedWS;

import java.util.List;

import me.khettaf.entities.Trajet;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by Me on 25/09/2017.
 */

public interface TrajetsInterface {
    @GET("trajets/all")
    Call<List<Trajet>> getAllTrajets(@Query("access_token") String accessToken);
}
