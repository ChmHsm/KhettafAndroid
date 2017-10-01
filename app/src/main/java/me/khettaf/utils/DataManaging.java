package me.khettaf.utils;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Me on 30/09/2017.
 */

public class DataManaging {

    public static String translateDate(@NonNull Long date){
        Locale locale = new Locale("fr", "FR");

        DateFormat timeFormatter = DateFormat.getTimeInstance(
                DateFormat.SHORT,
                locale);
        timeFormatter.format(new Date(date));

        Date today;
        String output;
        SimpleDateFormat dateFormatter;
        String pattern = "EEEE à ";

        dateFormatter = new SimpleDateFormat(pattern, locale);
        today = new Date();
        output = dateFormatter.format(today);

        return dateFormatter.format(today) + timeFormatter.format(new Date(date));
    }
}
