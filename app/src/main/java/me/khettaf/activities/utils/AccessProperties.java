package me.khettaf.activities.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Me on 24/09/2017.
 */

public class AccessProperties {

    public static String getProperty(Context context, String filename, String property){
        String propertyValue = "";
        try {
            JSONArray jsonArray = new JSONArray(Accessors.loadJSONFromAsset(context, filename));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            propertyValue = jsonObject.getString(property);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return propertyValue;
    }
}
