package lry.dip.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import lry.dip.database.DeviceConfiguration;
import lry.dip.database.DeviceConfigurationDAO;

/**
 * Created by rtison on 12/04/2018.
 * Usage :
 *      Intent intent = new Intent(this, DeviceConfigurationActivity.class);
 *      intent.putExtra(DeviceConfigurationActivity.KEY_EXTRA_DEVICE, bluetoothDevice);
 *      startActivity(intent);
 */

public class DeviceConfigurationActivity extends AppCompatActivity{
    public static final String KEY_EXTRA_DEVICE = "lry.dip.bluetooth.BLUETOOTH_DEVICE";
    private static final String DEBUG_TAG="DeviceConfiguration";

    private BluetoothDevice bluetoothDevice;
    private DeviceConfigurationDAO deviceConfigurationDAO;
    private DeviceConfiguration deviceConfiguration;

    private TextView textViewDeviceName;
    private RadioGroup radioGroup;
    private RadioButton radioButtonAuto;
    private RadioButton radioButtonManual;
    private Spinner spinnerLaunch;
    private Button buttonValider;

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_device_configuration);

        initComponents();
        initEvents();
        loadBluetoothDevice();
    }

    @Override
    protected void onDestroy(){
        deviceConfigurationDAO.close();
        super.onDestroy();
    }

    /**
     * Grise les parties selon l'état des boutons radios
     */
    private void autoDisableSections(){
        // Activation ou désactivation du spinner
        spinnerLaunch.setEnabled(radioButtonAuto.isChecked());
    }

    private String intentNameFromSpinnerValue(String value){
        if(value == getString(R.string.launcher_category_browser))
            return Intent.CATEGORY_APP_BROWSER;
        else if(value == getString(R.string.launcher_category_gps))
            return Intent.CATEGORY_APP_MAPS;
        else if(value == getString(R.string.launcher_category_music))
            return Intent.CATEGORY_APP_MUSIC;
        else
            return Intent.CATEGORY_DEFAULT;
    }

    /**
     * Méthode qui initialise les évènements
     */
    private void initEvents(){
        // Au changement d'un radio
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d(DEBUG_TAG, String.format("Radio %d checked", i));
                autoDisableSections();
            }
        });

        // Au clic sur le bouton valier
        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupération des valeurs
                String intentValue = (radioButtonAuto.isChecked() ? intentNameFromSpinnerValue((String)spinnerLaunch.getSelectedItem()) : null);
                deviceConfiguration.setLaunchIntent(intentValue);

                // Sauvegarde
                deviceConfigurationDAO.saveDeviceConfiguration(deviceConfiguration);

                // Redirection vers la page précédente
                Toast.makeText(DeviceConfigurationActivity.this, "La configuration pour l'appareil a bien été prise en compte", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    /**
     * Récupératoin des composents
     */
    private void initComponents(){
        textViewDeviceName = findViewById(R.id.textViewDeviceName);
        radioGroup = findViewById(R.id.radioGroup);
        radioButtonAuto = findViewById(R.id.radioButtonAuto);
        radioButtonManual = findViewById(R.id.radioButtonManual);
        spinnerLaunch = findViewById(R.id.spinnerLaunch);
        buttonValider = findViewById(R.id.buttonValider);
    }

    /**
     * Récupère les informations selon le BluetoothDevice
     */
    private void loadBluetoothDevice(){
        // Récupère le BluetoothDevice
        if(!getIntent().hasExtra(KEY_EXTRA_DEVICE))
            throw new IllegalArgumentException("Activity cannot find  extras " + KEY_EXTRA_DEVICE);
        bluetoothDevice = getIntent().getParcelableExtra(KEY_EXTRA_DEVICE);
        textViewDeviceName.setText(bluetoothDevice.getName());

        // Connexion à la base de données et récupération du DeviceConfiguration
        deviceConfigurationDAO = new DeviceConfigurationDAO(getApplicationContext());
        deviceConfigurationDAO.open();
        deviceConfiguration = deviceConfigurationDAO.getWithMacAddress(bluetoothDevice.getAddress());

        // Spinner initialisation
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.launch_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLaunch.setAdapter(adapter);

        // Check les radios selon l'intent
        String launchIntent = deviceConfiguration.getLaunchIntent();
        if(launchIntent!=null){
            radioButtonAuto.toggle();

            // Selection dans le spinner
            int itemNumber = spinnerLaunch.getCount();
            String tmpItem;
            for(int i = 0; i<itemNumber; i++){
                if(launchIntent.equals(intentNameFromSpinnerValue((String)spinnerLaunch.getItemAtPosition(i)))) {
                    spinnerLaunch.setSelection(i);
                    break;
                }
            }
        } else {
            radioButtonManual.toggle();
        }
    }
}
