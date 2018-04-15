package lry.dip.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Debug;
import android.util.Log;

import java.io.Console;

public class DeviceConfigurationDAO {
    private static final String DEBUG_STATE="DeviceConfigurationDAO";

    // Champs de la base de données
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = {SQLiteHelper.DEVICE_CONFIGURATION_COLUMN_MAC, SQLiteHelper.DEVICE_CONFIGURATION_COLUMN_LAUNCH_INTENT};

    public DeviceConfigurationDAO(Context context){
        dbHelper = new SQLiteHelper(context);
    }

    /**
     * Ouvre la connexion à la base de données
     */
    public void open(){
        database = dbHelper.getWritableDatabase();
        Log.d(DEBUG_STATE, "Connection to DB : " + database.isOpen());
    }

    /**
     * Ferme la connexion à la base de données
     */
    public void close(){
        dbHelper.close();
        Log.d(DEBUG_STATE, "Disconnect from DB : " + !database.isOpen());
    }

    /**
     * Sauvegarde un DeviceConfiguration en base de données
     * @param deviceConfiguration
     * @return
     */
    public boolean saveDeviceConfiguration(DeviceConfiguration deviceConfiguration){
        // Récupération des valeurs de la configuration
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.DEVICE_CONFIGURATION_COLUMN_MAC, deviceConfiguration.getMacAddress());
        values.put(SQLiteHelper.DEVICE_CONFIGURATION_COLUMN_LAUNCH_INTENT, deviceConfiguration.getLaunchIntent());

        // Requête d'insertion avec update si l'adresse mac existe déjà
        long insertId = database.insertWithOnConflict(SQLiteHelper.DEVICE_CONFIGURATION_TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);
        Log.d(DEBUG_STATE, "Saving " + deviceConfiguration);
        return (insertId != -1);
    }

    /**
     * Supprime un DeviceConfiguration de la base de données
     * @param deviceConfiguration
     */
    public void deleteDeviceConfiguration(DeviceConfiguration deviceConfiguration){
        String[] whereArgs = {deviceConfiguration.getMacAddress()};
        database.delete(SQLiteHelper.DEVICE_CONFIGURATION_TABLE_NAME, SQLiteHelper.DEVICE_CONFIGURATION_COLUMN_MAC + " = ?", whereArgs);
    }

    /**
     * Renvoi une instance de DeviceConfiguration depuis une adresse Mac
     * @param macAddress
     * @return
     */
    public DeviceConfiguration getWithMacAddress(String macAddress){
        // Récupératoin de la ligne
        String[] whereArgs = {macAddress};
        String selection = String.format("%s = ?", SQLiteHelper.DEVICE_CONFIGURATION_COLUMN_MAC);
        Cursor cursor = database.query(SQLiteHelper.DEVICE_CONFIGURATION_TABLE_NAME, allColumns, selection, whereArgs, null, null, null, null);

        // Récupération ou instanciation depuis la ligne
        DeviceConfiguration deviceConfiguration = (cursor.moveToFirst() ? cursorToDeviceConfiguration(cursor) : new DeviceConfiguration(macAddress));
        Log.d(DEBUG_STATE, "Getting " + deviceConfiguration);
        cursor.close(); // close cursor
        return deviceConfiguration; // return
    }

    /**
     * Crée une instance de DevceConfiguaration depuis un Cursor SQLite
     * @param cursor
     * @return
     */
    public DeviceConfiguration cursorToDeviceConfiguration(Cursor cursor){
        DeviceConfiguration deviceConfiguration = new DeviceConfiguration();
        deviceConfiguration.setMacAddress(cursor.getString(0));
        deviceConfiguration.setLaunchIntent(cursor.getString(1));
        return deviceConfiguration;
    }
}
