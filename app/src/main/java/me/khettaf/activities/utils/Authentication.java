package me.khettaf.activities.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import me.khettaf.R;

/**
 * Created by Me on 26/09/2017.
 */

public class Authentication {

    public static boolean isAuthenticationRequired(Context context) {

        SharedPreferences authPrefs = context.getSharedPreferences(
                context.getString(R.string.authentication_prefs), Context.MODE_PRIVATE);

        long dateOfTokenExpiration = authPrefs.getLong(
                context.getString(R.string.token_expires_at), 0);

        return dateOfTokenExpiration <= System.currentTimeMillis();
    }

    public static boolean isAccessTokenAvailable(Context context){
        SharedPreferences authPrefs = context.getSharedPreferences(
                context.getString(R.string.authentication_prefs), Context.MODE_PRIVATE);

        String accessToken = authPrefs.getString(context.getString(R.string.access_token), "");

        return !accessToken.equals("");
    }

    public static boolean isRefreshTokenAvailable(Context context){
        SharedPreferences authPrefs = context.getSharedPreferences(
                context.getString(R.string.authentication_prefs), Context.MODE_PRIVATE);

        String refreshToken = authPrefs.getString(context.getString(R.string.refresh_token), "");

        return !refreshToken.equals("");
    }
}
