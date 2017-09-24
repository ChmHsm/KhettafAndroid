package me.khettaf.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Me on 24/09/2017.
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "AppDatabase";

    public static final int VERSION = 1;
}
