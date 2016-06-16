package net.noratek.smartvoxxwear.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WearableListView;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import net.noratek.smartvoxx.common.model.Schedule;
import net.noratek.smartvoxx.common.utils.Constants;
import net.noratek.smartvoxxwear.R;
import net.noratek.smartvoxxwear.adapter.ScheduleGridPageAdapter;
import net.noratek.smartvoxxwear.event.SchedulesEvent;
import net.noratek.smartvoxxwear.wrapper.SchedulesListWrapper;

import java.util.List;

import pl.tajchert.buswear.EventBus;

/**
 * Created by eloudsa on 14/06/16.
 */
public class Schedule2Activity extends Activity implements GoogleApiClient.ConnectionCallbacks, DataApi.DataListener {

    private final static String TAG = Schedule2Activity.class.getCanonicalName();

    // Google Play Services
    private GoogleApiClient mApiClient;

    // Layout widgets and adapters
    private WearableListView mListView;

    // Avoid double tap
    private Boolean mClicked = false;

    // Conference information
    private String mCountryCode;
    private String mServerUrl;

    // Layout widgets and adapters
    private ScheduleGridPageAdapter mScheduleGridPageAdapter;
    private GridViewPager mPager;
    private DotsPageIndicator mDotsPageIndicator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // selected conference
        mCountryCode = "BE";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mCountryCode = bundle.getString(Constants.DATAMAP_COUNTRY);
            mServerUrl = bundle.getString(Constants.DATAMAP_SERVER_URL);
        }

        setContentView(R.layout.schedule_activity);
        mPager = (GridViewPager) findViewById(R.id.pager);

        // we prepare the view with initial values gathered from the Slot
        mScheduleGridPageAdapter = new ScheduleGridPageAdapter(this, getFragmentManager(), mCountryCode);
        mPager.setAdapter(mScheduleGridPageAdapter);

        mDotsPageIndicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
        mDotsPageIndicator.setPager(mPager);
    }


    @Override
    protected void onResume() {
        super.onResume();

        mClicked = false;

        // Retrieve and display the list of schedules
        getSchedulesFromCache(Constants.SCHEDULES_PATH + "/" + mCountryCode);

        getFavoriteFromCache();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();

        mApiClient.connect();
    }


    @Override
    protected void onStop() {
        if ((mApiClient != null) && (mApiClient.isConnected())) {
            Wearable.DataApi.removeListener(mApiClient, this);
            mApiClient.disconnect();
        }

        super.onStop();
    }


    private void sendMessage(final String path, final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // broadcast the message to all connected devices
                final NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mApiClient).await();
                for (Node node : nodes.getNodes()) {
                    Wearable.MessageApi.sendMessage(mApiClient, node.getId(), path, message.getBytes()).await();

                }
            }
        }).start();
    }


    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {

        for (DataEvent event : dataEventBuffer) {

            // Check if we have received our schedules
            if (event.getType() == DataEvent.TYPE_CHANGED && event.getDataItem().getUri().getPath().startsWith(Constants.SCHEDULES_PATH)) {

                SchedulesListWrapper schedulesListWrapper = new SchedulesListWrapper();

                final List<Schedule> schedulesList = schedulesListWrapper.getSchedulesList(event);

                EventBus.getDefault().postLocal(new SchedulesEvent(schedulesList));

                updateUI();

                return;
            }
        }

    }


    // Get Schedules from the data items repository (cache).
    // If not available, we refresh the data from the Mobile device.
    //
    private void getSchedulesFromCache(final String pathToContent) {
        Uri uri = new Uri.Builder()
                .scheme(PutDataRequest.WEAR_URI_SCHEME)
                .path(pathToContent)
                .build();

        Wearable.DataApi.getDataItems(mApiClient, uri)
                .setResultCallback(
                        new ResultCallback<DataItemBuffer>() {
                            @Override
                            public void onResult(DataItemBuffer dataItems) {

                                if (dataItems.getCount() == 0) {
                                    // refresh the list of schedules from Mobile
                                    sendMessage(pathToContent, mServerUrl);
                                    dataItems.release();
                                    return;
                                }

                                // retrieve the schedule from the cache
                                DataMap dataMap = DataMap.fromByteArray(dataItems.get(0).getData());
                                if (dataMap == null) {
                                    // unable to fetch data -> refresh the list of schedules from Mobile
                                    sendMessage(pathToContent, mServerUrl);
                                    dataItems.release();
                                    return;
                                }

                                // retrieve and display the schedule from the cache
                                SchedulesListWrapper schedulesListWrapper = new SchedulesListWrapper();

                                final List<Schedule> schedulesList = schedulesListWrapper.getSchedulesList(dataMap);

                                dataItems.release();

                                EventBus.getDefault().postLocal(new SchedulesEvent(schedulesList));

                                updateUI();
                            }
                        }
                );
    }


    // Get favorite status of the talk from the data item repository (cache).
    // If not available, we refresh the data from the Mobile device.
    //
    private void getFavoriteFromCache() {

        final String dataPath = Constants.FAVORITE_PATH;

        Uri uri = new Uri.Builder()
                .scheme(PutDataRequest.WEAR_URI_SCHEME)
                .path(dataPath)
                .build();

        Wearable.DataApi.getDataItems(mApiClient, uri)
                .setResultCallback(
                        new ResultCallback<DataItemBuffer>() {
                            @Override
                            public void onResult(DataItemBuffer dataItems) {

                                DataMap dataMap = null;
                                if (dataItems.getCount() > 0) {
                                    // retrieve the favorite from the cache
                                    dataMap = DataMap.fromByteArray(dataItems.get(0).getData());
                                }
/*
                                // retrieve the favorite from the cache
                                if (dataMap == null) {
                                    // Prepare the data map
                                    DataMap favoriteDataMap = new DataMap();
                                    favoriteDataMap.putString(Constants.DATAMAP_TALK_ID, talk.getId());
                                    favoriteDataMap.putString(Constants.DATAMAP_TITLE, talk.getTitle());
                                    favoriteDataMap.putLong(Constants.DATAMAP_FROM_TIME_MILLIS, talk.getFromTimeMillis());
                                    favoriteDataMap.putLong(Constants.DATAMAP_TO_TIME_MILLIS, talk.getToTimeMillis());

                                    // unable to fetch data -> retrieve the favorite status from the Mobile
                                    sendMessage(Constants.FAVORITE_PATH, favoriteDataMap.toByteArray());
                                    dataItems.release();
                                    return;
                                }

                                DataMap favoriteMap = dataMap.getDataMap(Constants.DETAIL_PATH);
                                if (favoriteMap == null) {
                                    dataItems.release();
                                    return;
                                }

                                mTalk.setEventId(favoriteMap.getLong(Constants.DATAMAP_EVENT_ID));
                                EventBus.getDefault().postLocal(new FavoriteEvent(mTalk.getEventId()));
*/

                                dataItems.release();
                            }
                        }
                );
    }


    private void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                //mScheduleGridPageAdapter.notifyDataSetChanged();
            }
        });
    }


    public void displaySlot(String dayName) {

        // display slots for this schedule
        Intent scheduleIntent = new Intent(Schedule2Activity.this, SlotActivity.class);

        Bundle b = new Bundle();
        b.putString(Constants.DATAMAP_SERVER_URL, mServerUrl);
        b.putString(Constants.DATAMAP_COUNTRY, mCountryCode);
        b.putString(Constants.DATAMAP_DAY_NAME, dayName);
        scheduleIntent.putExtras(b);

        Schedule2Activity.this.startActivity(scheduleIntent);

    }


    public void getSchedules() {
        // Retrieve and display the list of schedules
        getSchedulesFromCache(Constants.SCHEDULES_PATH + "/" + mCountryCode);
    }


    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

}