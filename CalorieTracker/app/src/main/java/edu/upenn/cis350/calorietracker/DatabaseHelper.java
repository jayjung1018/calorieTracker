package edu.upenn.cis350.calorietracker;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Jay Jung on 4/20/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CalorieTracker.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String BLOB_TYPE = " BLOB";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseContract.DataEntry.TABLE_NAME + " (" +
                    DatabaseContract.DataEntry._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.DataEntry.COLUMN_NAME_FBID + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_GOAL + INT_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_WEIGHT + TEXT_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_FAVFOOD + BLOB_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_RECFOOD + BLOB_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_FAVEX + BLOB_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_RECEX + BLOB_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_D1 + BLOB_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_D2 + BLOB_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_D3 + BLOB_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_D4 + BLOB_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_D5 + BLOB_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_D6 + BLOB_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_D7 + BLOB_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_D1E + BLOB_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_D2E + BLOB_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_D3E + BLOB_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_D4E + BLOB_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_D5E + BLOB_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_D6E + BLOB_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_D7E + BLOB_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_AFLAG + BLOB_TYPE + COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NAME_APROG + BLOB_TYPE +" )";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseContract.DataEntry.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
