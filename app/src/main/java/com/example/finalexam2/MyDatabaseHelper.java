package com.example.finalexam2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Todo";

    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table Tasks(_id integer primary key,image BLOB, name text not null,date text not null,done text not null);";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        //  database.execSQL("DROP TABLE IF EXISTS Tasks") /*Si volem reiniciar*/;
        // database.execSQL(DATABASE_CREATE);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(MyDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS Contactes");
        onCreate(database);
    }

}
