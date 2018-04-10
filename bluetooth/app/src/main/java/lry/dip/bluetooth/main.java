package lry.dip.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Set;

public class main extends AppCompatActivity {
    private Set<BluetoothDevice> devices;
    private BluetoothAdapter bluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
                        Toast.makeText(this, "Bluetooth aciver !!!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.buttonRecherche:
                // Test de l'existence de la fonctionalitée sur l'appareil
                if (bluetoothAdapter == null) {
                    Toast.makeText(this, "Bluetooth indisponible !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Active le bluetooth sans demander à l'utilsateur son accord ;)
                    if (bluetoothAdapter.isEnabled()) {
                        //liste des appareils déja connu
                        devices = bluetoothAdapter.getBondedDevices();
                        for (BluetoothDevice blueDevice : devices) {
                            System.out.println("Device = " + blueDevice.getName());
                            Toast.makeText(this, "Device = " + blueDevice.getName(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(this, "Veuillez activer le Bluetooth", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
