package lry.dip.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DeviceConfigurationDAO {
    // Champs de la base de données
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = {SQLiteHelper.DEVICE_CONFIGURATION_COLUMN_MAC, SQLiteHelper.DEVICE_CONFIGURATION_COLUMN_LAUNCH_INTENT};

    public DeviceConfigurationDAO(Context context){
        dbHelper = new SQLiteHelper(context);
    }

    public void open(){
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public boolean saveDeviceConfiguration(DeviceConfiguration deviceConfiguration){
        // Récupération des valeurs de la configuration
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.DEVICE_CONFIGURATION_COLUMN_MAC, deviceConfiguration.getMacAdress());
        values.put(SQLiteHelper.DEVICE_CONFIGURATION_COLUMN_LAUNCH_INTENT, deviceConfiguration.getLaunchIntent());

        // Requête d'insertion avec update si l'adresse mac existe déjà
        long insertId = database.insertWithOnConflict(SQLiteHelper.DEVICE_CONFIGURATION_TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);
        return (insertId != -1);
    }

    public void deleteDeviceConfiguration(DeviceConfiguration deviceConfiguration){
        String[] whereArgs = {deviceConfiguration.getMacAdress()};
        database.delete(SQLiteHelper.DEVICE_CONFIGURATION_TABLE_NAME, SQLiteHelper.DEVICE_CONFIGURATION_COLUMN_MAC + " = ?s", whereArgs);
    }

    public DeviceConfiguration getWithMacAdress(String macAdress){
        // Récupératoin de la ligne
        String[] whereArgs = {macAdress};
        String selection = String.format("%s = ?s", SQLiteHelper.DEVICE_CONFIGURATION_COLUMN_MAC);
        Cursor cursor = database.query(SQLiteHelper.DEVICE_CONFIGURATION_TABLE_NAME, allColumns, selection, whereArgs, null, null, null, null);

        // Récupération du premier élément et renvoi de l'objet
        DeviceConfiguration deviceConfiguration = null;
        if(cursor.moveToFirst())
            deviceConfiguration = cursorToDeviceConfiguration(cursor);

        // Close cursor
        cursor.close();

        // Return
        return deviceConfiguration;
    }

    public DeviceConfiguration cursorToDeviceConfiguration(Cursor cursor){
        DeviceConfiguration deviceConfiguration = new DeviceConfiguration();
        deviceConfiguration.setMacAdress(cursor.getString(0));
        deviceConfiguration.setLaunchIntent(cursor.getString(1));
        return deviceConfiguration;
    }
}
