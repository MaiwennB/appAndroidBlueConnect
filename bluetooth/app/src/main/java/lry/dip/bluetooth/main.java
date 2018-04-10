package lry.dip.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class main extends AppCompatActivity implements View.OnClickListener {
    // Bluetooth
    private Set<BluetoothDevice> devices;
    private BluetoothAdapter bluetoothAdapter;
    // Boutons de l'interface
    private Button btnActiver;
    private Button btnRechercher;
    private ListView listDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        btnActiver = (Button)findViewById(R.id.buttonActive);
        btnActiver.setOnClickListener(this);

        btnRechercher = (Button)findViewById(R.id.buttonRecherche);
        btnRechercher.setOnClickListener(this);

        listDevice = (ListView)findViewById(R.id.listDevice);
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
                break;


            case R.id.buttonRecherche:
                // Test de l'existence de la fonctionalitée sur l'appareil
                if (bluetoothAdapter == null) {
                    Toast.makeText(this, "Bluetooth indisponible !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (bluetoothAdapter.isEnabled()) {
                        //liste des appareils déja connu
                        String deviceL = "";
                        devices = bluetoothAdapter.getBondedDevices();
                        for (BluetoothDevice blueDevice : devices) {
                            System.out.println("Device = " + blueDevice.getName());
                            //Toast.makeText(this, "Device = " + blueDevice.getName(), Toast.LENGTH_SHORT).show();
                            deviceL = deviceL+blueDevice.getName()+" ; ";
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_list_item_1, deviceL.split(" ; "));
                        listDevice.setAdapter(adapter);

                    }
                    else{
                        Toast.makeText(this, "Veuillez activer le Bluetooth", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}