package lry.dip.bluetooth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by rtison on 12/04/2018.
 */

public class DeviceConfigurationActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_device_configuration);
    }

    /**
     * On passe à la vue une instance de BluetoothDevice
     * Mise à jour de la table en BDD
     */
}
