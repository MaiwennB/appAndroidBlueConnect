package lry.dip.bluetooth;

import android.app.LauncherActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Set;

import lry.dip.database.DeviceConfiguration;
import lry.dip.database.DeviceConfigurationDAO;
import lry.dip.launcher.launcher;

public class main extends AppCompatActivity implements View.OnClickListener {
    // Bluetooth
    private Set<BluetoothDevice> devices;
    private BluetoothAdapter bluetoothAdapter;
    private DeviceConfigurationDAO deviceConfigurationDAO;

    // Boutons de l'interface
    private Button btnActiver;
    private Button btnAfficher;
    private Button btnRechercher;
    private ListView listDevice;
    private TextView textDeviceC;

    private ArrayList<String> mlistItems=new ArrayList<String>();
    private ArrayAdapter<String> madapter;
    private ListView mListDevice;


    private ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        this.btnActiver = (Button) findViewById(R.id.buttonActive);
        this.btnActiver.setOnClickListener(this);

        this.btnAfficher = (Button) findViewById(R.id.buttonAffiche);
        this.btnAfficher.setOnClickListener(this);

        this.btnRechercher = (Button) findViewById(R.id.buttonRecherche);
        this.btnRechercher.setOnClickListener(this);

        // Récupération DAO
        this.deviceConfigurationDAO = new DeviceConfigurationDAO(getApplicationContext());
        this.deviceConfigurationDAO.open();

        this.listDevice = (ListView) findViewById(R.id.listDevice);
        this.listDevice.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(main.this, DeviceConfigurationActivity.class);
                intent.putExtra(DeviceConfigurationActivity.KEY_EXTRA_DEVICE, (BluetoothDevice) devices.toArray()[i]);
                startActivity(intent);
                return true;
            }
        });

        listDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Récupération de l'intent à partir du BluetoothDevice
                BluetoothDevice device = (BluetoothDevice) devices.toArray()[i];
                DeviceConfiguration deviceConfiguration = deviceConfigurationDAO.getWithMacAddress(device.getAddress());
                Intent intent = deviceConfiguration.getLaunchIntent();
                if (intent == null)
                    intent = new Intent(main.this, launcher.class);

                // Verify that the intent will resolve to an activity
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivity(intent);
            }
        });

        this.textDeviceC = (TextView) findViewById(R.id.text_devices_connu);
        this.textDeviceC.setText(R.string.textDC);
        this.textDeviceC.setVisibility(View.INVISIBLE);

        this.mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

    }

    public void onClick(View pView) {
        switch (pView.getId()) {
            case R.id.buttonActive:
                // ---- Activation du bluetooth ----
                // Test si l'appareil e
                if (this.bluetoothAdapter == null) {
                    Toast.makeText(this, "Bluetooth indisponible", Toast.LENGTH_SHORT).show();
                } else {
                    if (!this.bluetoothAdapter.isEnabled()) {
                        this.bluetoothAdapter.enable();
                        Toast.makeText(this, "Bluetooth activé", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Bluetooth déjà activé", Toast.LENGTH_SHORT).show();
                    }
                }
                break;


            case R.id.buttonAffiche:
                // Test de l'existence de la fonctionalitée sur l'appareil
                if (this.bluetoothAdapter == null) {
                    Toast.makeText(this, "Bluetooth indisponible", Toast.LENGTH_SHORT).show();
                } else {
                    if (this.bluetoothAdapter.isEnabled()) {
                        //liste des appareils déja connu
                        String deviceL = "";
                        this.devices = bluetoothAdapter.getBondedDevices();
                        for (BluetoothDevice blueDevice : this.devices) {
                            this.ajouterDeviceListe(blueDevice.getName());
                        }
                        this.textDeviceC.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(this, "Veuillez activer le Bluetooth", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.buttonRecherche:

                //enregistrement pour les diffusions lors de l'arrivée d'un nouvel appareil
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver, filter);

                // rendre l'appareil visible
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);

                bluetoothAdapter.startDiscovery();

                //mReceiver.onReceive(context, );
                break;
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                main.this.ajouterDeviceListe(device.getName());
            }
        }
    };

    protected void onDestroy() {
        deviceConfigurationDAO.close();
        super.onDestroy();
        bluetoothAdapter.cancelDiscovery();
        unregisterReceiver(mReceiver);
    }
    protected void ajouterDeviceListe(String pNomDevice){
        this.mAdapter.add(pNomDevice);
        this.listDevice.setAdapter(this.mAdapter);
    }
}
