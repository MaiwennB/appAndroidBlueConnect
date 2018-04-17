package lry.dip.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    // devices_configurations
    public static final String DEVICE_CONFIGURATION_COLUMN_MAC="mac_adress"; // column mac_adress
    public static final String DEVICE_CONFIGURATION_COLUMN_LAUNCH_INTENT="launch_intent"; // column launch_intent
    public static final String DEVICE_CONFIGURATION_TABLE_NAME="devices_configurations"; // table name
    public static final String DEVICE_CONFIGURATION_TABLE_CREATE= // create table script
        "CREATE TABLE " + DEVICE_CONFIGURATION_TABLE_NAME + " (\n" +
        "\t" + DEVICE_CONFIGURATION_COLUMN_MAC + " STRING NOT NULL PRIMARY KEY,\n" +
        "\t" + DEVICE_CONFIGURATION_COLUMN_LAUNCH_INTENT + " string\n" +
        ");";
    public static final String DEVICE_CONFIGUATION_TABLE_DROP="DROP TABLE IF EXISTS " + DEVICE_CONFIGURATION_TABLE_NAME + ";"; // drop table script

    // DB configuration
    private static final String DATABASE_NAME="blueconnect.db"; // db name
    private static final int DATABASE_VERSION=3; // db version

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) { sqLiteDatabase.execSQL(DEVICE_CONFIGURATION_TABLE_CREATE); }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DEVICE_CONFIGUATION_TABLE_DROP);
        onCreate(sqLiteDatabase);
    }
}
