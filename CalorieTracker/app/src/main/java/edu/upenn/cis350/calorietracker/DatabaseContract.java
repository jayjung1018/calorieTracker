package edu.upenn.cis350.calorietracker;

import android.provider.BaseColumns;

/**
 * Created by Jay Jung on 4/20/2016.
 */
public class DatabaseContract  {

    public DatabaseContract() {}

    /* Inner class that defines the table contents */
    public static abstract class DataEntry implements BaseColumns {
        public static final String TABLE_NAME = "UserInfo";
        public static final String COLUMN_NAME_FBID = "fbid";
        public static final String COLUMN_NAME_GOAL = "goal";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_FAVFOOD = "favfood";
        public static final String COLUMN_NAME_RECFOOD = "recfood";
        public static final String COLUMN_NAME_FAVEX = "favex";
        public static final String COLUMN_NAME_RECEX = "recex";
        public static final String COLUMN_NAME_D1 = "d1";
        public static final String COLUMN_NAME_D2 = "d2";
        public static final String COLUMN_NAME_D3 = "d3";
        public static final String COLUMN_NAME_D4 = "d4";
        public static final String COLUMN_NAME_D5 = "d5";
        public static final String COLUMN_NAME_D6 = "d6";
        public static final String COLUMN_NAME_D7 = "d7";
        public static final String COLUMN_NAME_D1E = "d1e";
        public static final String COLUMN_NAME_D2E = "d2e";
        public static final String COLUMN_NAME_D3E = "d3e";
        public static final String COLUMN_NAME_D4E = "d4e";
        public static final String COLUMN_NAME_D5E = "d5e";
        public static final String COLUMN_NAME_D6E = "d6e";
        public static final String COLUMN_NAME_D7E = "d7e";
        public static final String COLUMN_NAME_AFLAG = "aflag";
        public static final String COLUMN_NAME_APROG = "aprog";

    }

}
