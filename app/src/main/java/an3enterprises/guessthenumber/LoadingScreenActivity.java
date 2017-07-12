package an3enterprises.guessthenumber;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;

public class LoadingScreenActivity extends Activity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{
    static GoogleApiClient mGoogleApiClient;
    static boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_loading_screen);

        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES).addConnectionCallbacks(this)
                .addApi(Drive.API).addScope(Drive.SCOPE_APPFOLDER)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Intent intent = new Intent(LoadingScreenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Google Play Games Connected", Toast.LENGTH_SHORT).show();
        isConnected = true;
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Google Play Games Connection suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()){
            try{
                connectionResult.startResolutionForResult(this, 0);
                Toast.makeText(this, "Can't connect to Google Play. Reopen the app.", Toast.LENGTH_SHORT).show();
                mGoogleApiClient.connect();
            }catch (IntentSender.SendIntentException e){
                mGoogleApiClient.connect();
            }
        }
    }
}
