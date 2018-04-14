package lry.dip.launcher;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import lry.dip.bluetooth.R;

public class launcher extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnMusique;
    private Button mBtnGps;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);//Définir la vue

        //Gestion des boutons
        this.mBtnMusique = findViewById(R.id.btn_lnc_music);
        this.mBtnGps = findViewById(R.id.btn_lnc_gps);
        //Création des listenners sur les boutons
        this.mBtnMusique.setOnClickListener(this);
        this.mBtnGps.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_lnc_music){
            Toast.makeText(getApplicationContext(),"Bouton Musique",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
            startActivity(intent);
        }
        else if(view.getId() == R.id.btn_lnc_gps){
            Toast.makeText(getApplicationContext(),"Bouton GPS",Toast.LENGTH_LONG).show();
        }
    }
}
