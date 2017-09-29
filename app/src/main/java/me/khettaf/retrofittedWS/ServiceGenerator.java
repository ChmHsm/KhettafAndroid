package me.khettaf.retrofittedWS;

import android.app.Service;

import com.raizlabs.android.dbflow.config.GeneratedDatabaseHolder;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static me.khettaf.activities.utils.Authentication.isAuthenticationRequired;

/**
 * Created by Me on 25/09/2017.
 */

public class ServiceGenerator {

    public static final String API_BASE_URL = "https://khettafws.herokuapp.com/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
