package net.noratek.smartvoxxwear.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import net.noratek.smartvoxx.common.utils.Constants;
import net.noratek.smartvoxxwear.R;

public class MobileActivity extends AppCompatActivity {

    private  GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mobile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(Wearable.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                            Uri uri = new Uri.Builder()
                                    .scheme(PutDataRequest.WEAR_URI_SCHEME)
                                    .path(Constants.SPEAKER_PATH)
                                    .build();

                            Wearable.DataApi.deleteDataItems(googleApiClient, uri, DataApi.FILTER_PREFIX);
                        }

                        @Override
                        public void onConnectionSuspended(int cause) {

                        }
                    }).build();
            googleApiClient.connect();



            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
