package net.noratek.smartvoxxwear.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import net.noratek.smartvoxx.common.utils.Constants;
import net.noratek.smartvoxxwear.R;

public class MobileActivity extends AppCompatActivity {

    private final static String TAG = MobileActivity.class.getCanonicalName();

    private GoogleApiClient mGoogleApiClient;
    private String mVersionName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);


        // Retrieve the application version
        try {
            PackageManager manager = getApplicationContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    getApplicationContext().getPackageName(), 0);

            mVersionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, e.getMessage());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mobile, menu);

        MenuItem versionMenuItem = menu.findItem(R.id.action_clear_cache);
        versionMenuItem.setTitle(getString(R.string.refresh));

        versionMenuItem = menu.findItem(R.id.action_version);
        versionMenuItem.setTitle(getString(R.string.app_version) + " " + mVersionName);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Refresh the local cache used by the watch
        if (id == R.id.action_clear_cache) {
            try {
                showDialog();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Delete the Data API cache used by the smartwatch.
    // This feature allows the watch to received the latest updates from the Phone.
    //
    private void deleteItems(String dataPath) {

        Uri uri = new Uri.Builder()
                .scheme(PutDataRequest.WEAR_URI_SCHEME)
                .path(dataPath)
                .build();

        Wearable.DataApi.deleteDataItems(mGoogleApiClient, uri, DataApi.FILTER_PREFIX);

    }

    private void showDialog() throws Exception {
        AlertDialog.Builder builder = new AlertDialog.Builder(MobileActivity.this);

        builder.setMessage(getString(R.string.dialog_message));

        builder.setTitle(getString(R.string.refresh));

        builder.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                        .addApi(Wearable.API)
                        .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(Bundle bundle) {
                                deleteItems(Constants.FAVORITE_PATH);
                                deleteItems(Constants.CONFERENCES_PATH);
                                deleteItems(Constants.SCHEDULES_PATH);
                                deleteItems(Constants.SLOTS_PATH);
                                deleteItems(Constants.TALK_PATH);
                                deleteItems(Constants.SPEAKER_PATH);

                                Toast.makeText(MobileActivity.this, getString(R.string.clear_cache_info), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onConnectionSuspended(int cause) {
                            }
                        }).build();
                mGoogleApiClient.connect();

            }
        });

        builder.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

}
