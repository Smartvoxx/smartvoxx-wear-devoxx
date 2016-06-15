package net.noratek.smartvoxxwear.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.noratek.smartvoxx.common.model.Schedule;
import net.noratek.smartvoxx.common.utils.Constants;
import net.noratek.smartvoxxwear.R;
import net.noratek.smartvoxxwear.activity.Schedule2Activity;
import net.noratek.smartvoxxwear.event.SchedulesEvent;

import java.util.ArrayList;
import java.util.List;

import pl.tajchert.buswear.EventBus;

/**
 * Created by eloudsa on 14/06/16.
 */
public class ScheduleFragment extends Fragment implements WearableListView.ClickListener {

    private final static String TAG = TalkFragment.class.getCanonicalName();

    private View mMainView;

    // Layout widgets and adapters
    private WearableListView mListView;

    private ListViewAdapter mListViewAdapter;

    // Avoid double tap
    private Boolean mClicked = false;

    List<Schedule> mSchedulesList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        final String pageTitle = (getArguments() != null ? getArguments().getString(Constants.DATAMAP_TITLE) : "");


        mMainView = inflater.inflate(R.layout.schedule_fragment, container, false);
        WatchViewStub stub = (WatchViewStub) mMainView.findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

                // change the title
                ((TextView) mMainView.findViewById(R.id.title)).setText(getString(R.string.welcome_devoxx) + " " + pageTitle);

                // Listview component
                mListView = (WearableListView) mMainView.findViewById(R.id.wearable_list);

                // Assign the adapter
                mListViewAdapter = new ListViewAdapter(getActivity(), new ArrayList<Schedule>());
                mListView.setAdapter(mListViewAdapter);

                // Set the click listener
                mListView.setClickListener(ScheduleFragment.this);

                // allow scrolling inside a grid view pager
                mListView.setGreedyTouchMode(true);
            }
        });


        return mMainView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mClicked = false;

        if (mSchedulesList == null) {
            ((Schedule2Activity) getActivity()).getSchedules();
        } else {
            refreshListView();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {

        // Avoid double tap
        if (mClicked) {
            return;
        }

        Schedule schedule = (Schedule) viewHolder.itemView.getTag();
        if (schedule == null) {
            return;
        }

        mClicked = true;

        // display the slot for the selected day
        ((Schedule2Activity) getActivity()).displaySlot(schedule.getDay());
    }


    @Override
    public void onTopEmptyRegionClick() {

    }


    public void onEvent(final SchedulesEvent schedulesEvent) {

        if (schedulesEvent == null) {
            return;
        }

        mSchedulesList = schedulesEvent.getSchedulesList();

        refreshListView();
    }



    private void refreshListView() {

        if (getActivity() == null) {
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMainView.findViewById(R.id.progressBar).setVisibility(View.GONE);
                mListViewAdapter.refresh(mSchedulesList);
            }
        });


    }


    // Inner class providing the WearableListview's adapter
    public class ListViewAdapter extends WearableListView.Adapter {
        private List<Schedule> mDataset;
        private final Context mContext;

        // Provide a suitable constructor (depends on the kind of dataset)
        public ListViewAdapter(Context context, List<Schedule> schedulesList) {
            mContext = context;
            this.mDataset = schedulesList;
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

            // retrieve, transform and display the schedule's day
            Schedule schedule = mDataset.get(position);
            String scheduleDay = schedule.getTitle().replace(",", "\n");
            view.setText(scheduleDay);

            // replace list item's metadata
            holder.itemView.setTag(schedule);
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

        public void refresh(List<Schedule> schedulesList) {
            mDataset = schedulesList;

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
