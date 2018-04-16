package lry.dip.launcher;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import lry.dip.bluetooth.R;
import lry.dip.intent.IntentHelper;

public class launcher extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnMusique;
    private Button mBtnGps;
    private Button mBtnYoutube;
    private Intent mIntentionApplication;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);//Définir la vue

        //Gestion des boutons
        this.mBtnMusique = findViewById(R.id.btn_lnc_music);
        this.mBtnGps = findViewById(R.id.btn_lnc_gps);
        this.mBtnYoutube = findViewById(R.id.btn_lnc_youtube);
        //Création des listenners sur les boutons
        this.mBtnMusique.setOnClickListener(this);
        this.mBtnGps.setOnClickListener(this);
        this.mBtnYoutube.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //Bouton de lancement de la musique
        if(view.getId() == R.id.btn_lnc_music){
            this.mIntentionApplication = IntentHelper.intentMusic();
            startActivity(this.mIntentionApplication);
        }
        //Bouton de lancement du GPS
        else if(view.getId() == R.id.btn_lnc_gps){
            this.mIntentionApplication = IntentHelper.intentGPS();
            if (this.mIntentionApplication.resolveActivity(getPackageManager()) != null) {
                startActivity(this.mIntentionApplication);
            }
        }
        //Bouton de lancement de Youtube
        else if(view.getId() == R.id.btn_lnc_youtube){
            this.mIntentionApplication = IntentHelper.intentYoutube();
            startActivity(this.mIntentionApplication);
        }
    }
}
