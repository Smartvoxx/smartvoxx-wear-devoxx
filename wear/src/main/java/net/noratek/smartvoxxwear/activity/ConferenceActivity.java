package net.noratek.smartvoxxwear.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

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

import net.noratek.smartvoxx.common.model.Conference;
import net.noratek.smartvoxx.common.utils.Constants;
import net.noratek.smartvoxxwear.R;
import net.noratek.smartvoxxwear.wrapper.ConferencesListWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by eloudsa on 12/11/15.
 */
public class ConferenceActivity extends Activity implements WearableListView.ClickListener, GoogleApiClient.ConnectionCallbacks, DataApi.DataListener {

    private final static String TAG = ConferenceActivity.class.getCanonicalName();

    // Google Play Services
    private GoogleApiClient mApiClient;

    // Layout widgets and adapters
    private WearableListView mListView;
    private ListViewAdapter mListViewAdapter;

    // Avoid double tap
    private Boolean mClicked = false;

    //create a counter to count the number of instances of this activity
    public static AtomicInteger mActivitiesLaunched = new AtomicInteger(0);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // if launching will create more than one instance of this activity, bail out
        if (mActivitiesLaunched.incrementAndGet() > 1) {
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.conference_activity);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

                // Listview component
                mListView = (WearableListView) findViewById(R.id.wearable_list);

                // Assign the adapter
                mListViewAdapter = new ListViewAdapter(ConferenceActivity.this, new ArrayList<Conference>());
                mListView.setAdapter(mListViewAdapter);

                // Set the click listener
                mListView.setClickListener(ConferenceActivity.this);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        mClicked = false;

        // Retrieve and display the list of schedules
        getConferencesFromCache(Constants.CONFERENCES_PATH);
    }

    @Override
    protected void onDestroy() {
        //remove this activity from the counter
        mActivitiesLaunched.getAndDecrement();

        super.onDestroy();
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
            if (event.getType() == DataEvent.TYPE_CHANGED && event.getDataItem().getUri().getPath().startsWith(Constants.CONFERENCES_PATH)) {

                ConferencesListWrapper conferencesListWrapper = new ConferencesListWrapper();

                final List<Conference> conferencesList = conferencesListWrapper.getConferencesList(event);

                updateUI(conferencesList);

                return;
            }
        }

    }


    // Get Conferences from the data items repository (cache).
    // If not available, we refresh the data from the Mobile device.
    //
    private void getConferencesFromCache(final String pathToContent) {
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
                                    // refresh the list of conferences from Mobile
                                    sendMessage(pathToContent, "get list of conferences");
                                    dataItems.release();
                                    return;
                                }

                                DataMap dataMap = DataMap.fromByteArray(dataItems.get(0).getData());
                                if (dataMap == null) {
                                    // refresh the list of conferences from Mobile
                                    sendMessage(pathToContent, "get list of conferences");
                                    dataItems.release();
                                    return;
                                }

                                // retrieve and display the conferences from the cache
                                ConferencesListWrapper conferencesListWrapper = new ConferencesListWrapper();

                                final List<Conference> conferencesList = conferencesListWrapper.getConferencesList(dataMap);

                                dataItems.release();

                                updateUI(conferencesList);
                            }
                        }
                );
    }


    private void updateUI(final List<Conference> conferencesList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                mListViewAdapter.refresh(conferencesList);
            }
        });
    }


    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {

        // Avoid double tap
        if (mClicked) {
            return;
        }

        Conference conference = (Conference) viewHolder.itemView.getTag();
        if (conference == null) {
            return;
        }

        mClicked = true;

        // display schedules for this conference
        Intent scheduleIntent = new Intent(ConferenceActivity.this, Schedule2Activity.class);

        Bundle b = new Bundle();
        b.putString(Constants.DATAMAP_COUNTRY, conference.getCountryCode());
        b.putString(Constants.DATAMAP_SERVER_URL, conference.getServerUrl());
        scheduleIntent.putExtras(b);

        ConferenceActivity.this.startActivity(scheduleIntent);
    }

    @Override
    public void onTopEmptyRegionClick() {

    }


    // Inner class providing the WearableListview's adapter
    public class ListViewAdapter extends WearableListView.Adapter {
        private List<Conference> mDataset;
        private final Context mContext;

        // Provide a suitable constructor (depends on the kind of dataset)
        public ListViewAdapter(Context context, List<Conference> conferencesList) {
            mContext = context;
            this.mDataset = conferencesList;
        }

        // Provide a reference to the type of views we're using
        public class ItemViewHolder extends WearableListView.ViewHolder {
            private TextView textView;

            public ItemViewHolder(View itemView) {
                super(itemView);
                // find the text view within the custom item's layout
                textView = (TextView) itemView.findViewById(R.id.description);
            }
        }

        // Create new views for list items
        // (invoked by the WearableListView's layout manager)
        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
            // Inflate our custom layout for list items
            return new ItemViewHolder(new SettingsItemView(mContext));
        }

        // Replace the contents of a list item
        // Instead of creating new views, the list tries to recycle existing ones
        // (invoked by the WearableListView's layout manager)
        @Override
        public void onBindViewHolder(WearableListView.ViewHolder holder,
                                     int position) {

            // retrieve the text view
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            TextView view = itemHolder.textView;

            // retrieve, transform and display the conference
            Conference conference= mDataset.get(position);
            view.setText(conference.getTitle());

            // replace list item's metadata
            holder.itemView.setTag(conference);
        }

        // Return the size of your dataset
        // (invoked by the WearableListView's layout manager)
        @Override
        public int getItemCount() {
            if (mDataset == null) {
                return 0;
            }
            return mDataset.size();
        }

        public void refresh(List<Conference> conferencesList) {
            mDataset = conferencesList;

            Collections.sort(mDataset, new Comparator<Conference>() {
                @Override
                public int compare(Conference conferenceA, Conference conferenceB) {
                    return conferenceA.getCountryCode().compareTo(conferenceB.getCountryCode());
                }
            });

            // reload the listview
            notifyDataSetChanged();
        }


        // Static nested class used to animate the listview's item
        public final class SettingsItemView extends FrameLayout implements WearableListView.OnCenterProximityListener {

            private TextView description;

            public SettingsItemView(Context context) {
                super(context);
                View.inflate(context, R.layout.schedule_row_fragment, this);

                description = (TextView) findViewById(R.id.description);
            }

            @Override
            public void onCenterPosition(boolean b) {
                description.animate().scaleX(1f).scaleY(1f).alpha(1);
            }

            @Override
            public void onNonCenterPosition(boolean b) {
                description.animate().scaleX(0.8f).scaleY(0.8f).alpha(0.6f);
            }
        }

    }

}
