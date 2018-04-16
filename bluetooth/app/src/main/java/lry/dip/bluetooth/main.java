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

import java.util.Set;

import lry.dip.database.DeviceConfiguration;
import lry.dip.database.DeviceConfigurationDAO;

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

    //La liste pour les devices
    private String deviceL = "";


    private Intent mIntentionActivtite2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        btnActiver = (Button)findViewById(R.id.buttonActive);
        btnActiver.setOnClickListener(this);

        btnAfficher = (Button)findViewById(R.id.buttonAffiche);
        btnAfficher.setOnClickListener(this);

        btnRechercher = (Button)findViewById(R.id.buttonRecherche);
        btnRechercher.setOnClickListener(this);

        // Récupération DAO
        deviceConfigurationDAO = new DeviceConfigurationDAO(getApplicationContext());
        deviceConfigurationDAO.open();

        listDevice = (ListView)findViewById(R.id.listDevice);
        listDevice.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(main.this, DeviceConfigurationActivity.class);
                intent.putExtra(DeviceConfigurationActivity.KEY_EXTRA_DEVICE, (BluetoothDevice)devices.toArray()[i]);
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
                if(intent == null)
                    intent = new Intent(main.this, lry.dip.launcher.launcher.class);

                // Verify that the intent will resolve to an activity
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivity(intent);
            }
        });

        textDeviceC = (TextView)findViewById(R.id.text_devices_connu);
        textDeviceC.setText(R.string.textDC);
        textDeviceC.setVisibility(View.INVISIBLE);
    }

    public void onClick(View pView){
        switch (pView.getId()){
            case R.id.buttonActive:
                // ---- Activation du bluetooth ----
                // Test si l'appareil e
                if (bluetoothAdapter == null) {
                    Toast.makeText(this, "Bluetooth indisponible !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!bluetoothAdapter.isEnabled()) {
                        bluetoothAdapter.enable();
                        Toast.makeText(this, "Bluetooth activer !!!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, "Bluetooth déja activer !!!", Toast.LENGTH_SHORT).show();
                    }
                }
                this.mIntentionActivtite2 = new Intent(main.this, lry.dip.launcher.launcher.class);
                startActivity(this.mIntentionActivtite2);
                break;


            case R.id.buttonAffiche:
                // Test de l'existence de la fonctionalité sur l'appareil
                if (bluetoothAdapter == null) {
                    Toast.makeText(this, "Bluetooth indisponible !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (bluetoothAdapter.isEnabled()) {
                        //liste des appareils déja connu
                        this.deviceL = "";
                        devices = bluetoothAdapter.getBondedDevices();
                        for (BluetoothDevice blueDevice : devices) {
                            System.out.println("Device = " + blueDevice.getName());
                            //Toast.makeText(this, "Device = " + blueDevice.getName(), Toast.LENGTH_SHORT).show();
                            this.deviceL = this.deviceL+blueDevice.getName()+" ; ";
                        }
                        this.ajouterAppareilListe(this.deviceL);

                    }
                    else{
                        Toast.makeText(this, "Veuillez activer le Bluetooth", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            //Elodie
            case R.id.buttonRecherche:
                //Remettre la liste des appareils affichés à 0
                this.deviceL = "";
                //enregistrement pour les diffusions lors de l'arrivée d'un nouvel appareil
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver, filter);

                // rendre l'appareil visible
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);

                bluetoothAdapter.startDiscovery();

                break;
            //fin Elodie
        }
    }

    //Elodie
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Toast.makeText(context, "Nouvel appareil = " + device.getName(), Toast.LENGTH_SHORT).show();
                main.this.deviceL = device.getName()+";";
                main.this.ajouterAppareilListe(main.this.deviceL);
            }
        }
    };

    protected void onDestroy(){
        deviceConfigurationDAO.close();
        super.onDestroy();
        bluetoothAdapter.cancelDiscovery();
        unregisterReceiver(mReceiver);
    }
    //fin Elodie
    protected void ajouterAppareilListe(String pListeAppareil){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pListeAppareil.split(" ; "));
        listDevice.setAdapter(adapter);
        textDeviceC.setVisibility(View.VISIBLE);
    };
}
