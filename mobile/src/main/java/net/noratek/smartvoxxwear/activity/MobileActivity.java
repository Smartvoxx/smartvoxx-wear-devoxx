package net.noratek.smartvoxxwear.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import net.noratek.smartvoxx.common.utils.Constants;
import net.noratek.smartvoxx.common.wear.GoogleApiConnector;
import net.noratek.smartvoxxwear.R;

public class MobileActivity extends AppCompatActivity {

    private final static String TAG = MobileActivity.class.getCanonicalName();

    private String mVersionName;

    private GoogleApiConnector mGoogleApiConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);


        mGoogleApiConnector = new GoogleApiConnector(this);

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
    protected void onStop() {
        mGoogleApiConnector.disconnect();
        super.onStop();
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



    private void showDialog() throws Exception {
        AlertDialog.Builder builder = new AlertDialog.Builder(MobileActivity.this);

        builder.setMessage(getString(R.string.dialog_message));

        builder.setTitle(getString(R.string.refresh));

        builder.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mGoogleApiConnector.deleteAllItems(Constants.CONFERENCES_PATH);
                mGoogleApiConnector.deleteAllItems(Constants.FAVORITE_PATH);
                mGoogleApiConnector.deleteAllItems(Constants.SCHEDULES_PATH);
                mGoogleApiConnector.deleteAllItems(Constants.SLOTS_PATH);
                mGoogleApiConnector.deleteAllItems(Constants.TALK_PATH);
                mGoogleApiConnector.deleteAllItems(Constants.SPEAKER_PATH);

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
