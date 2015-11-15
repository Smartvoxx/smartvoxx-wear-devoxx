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

    private  GoogleApiClient mGoogleApiClient;


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

            mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(Wearable.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                            deleteItems(Constants.FAVORITE_PATH);
                            deleteItems(Constants.SPEAKER_PATH);
                            deleteItems(Constants.CONFERENCES_PATH);
                            deleteItems(Constants.SCHEDULES_PATH);
                            deleteItems(Constants.SLOTS_PATH);
                            deleteItems(Constants.SPEAKERS_PATH);
                            deleteItems(Constants.TALK_PATH);
                            deleteItems(Constants.SPEAKERS_PATH);
                        }

                        @Override
                        public void onConnectionSuspended(int cause) {

                        }
                    }).build();
            mGoogleApiClient.connect();



            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void deleteItems(String dataPath) {

        Uri uri = new Uri.Builder()
                .scheme(PutDataRequest.WEAR_URI_SCHEME)
                .path(dataPath)
                .build();

        Wearable.DataApi.deleteDataItems(mGoogleApiClient, uri, DataApi.FILTER_PREFIX);

    }
}
