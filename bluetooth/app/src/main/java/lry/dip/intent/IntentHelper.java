package lry.dip.intent;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public class IntentHelper {
    public static Intent intentMusic(){
        return new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
    }

    public static Intent intentGPS(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage("com.google.android.apps.maps");
        return intent;
    }

    public static Intent intentYoutube(){
        return new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/"));
    }
}
