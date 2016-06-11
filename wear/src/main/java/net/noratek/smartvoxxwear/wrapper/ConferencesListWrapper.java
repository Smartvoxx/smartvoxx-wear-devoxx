package net.noratek.smartvoxxwear.wrapper;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;

import net.noratek.smartvoxx.common.model.Conference;
import net.noratek.smartvoxx.common.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eloudsa on 03/09/15.
 */
public class ConferencesListWrapper {

    public List<Conference> getConferencesList(DataEvent dataEvent) {

        if (dataEvent == null) {
            return null;
        }

        DataMapItem dataMapItem = DataMapItem.fromDataItem(dataEvent.getDataItem());
        if (dataMapItem == null) {
            return null;
        }

        return getConferencesList(dataMapItem.getDataMap());
    }


    public List<Conference> getConferencesList(DataMap dataMap) {

        if (dataMap == null) {
            return null;
        }

        List<DataMap> conferencesDataMap = dataMap.getDataMapArrayList(Constants.LIST_PATH);
        if (conferencesDataMap == null) {
            return null;
        }

        List<Conference> conferencesList = new ArrayList<>();

        for (DataMap conferenceDataMap : conferencesDataMap) {
            // retrieve the speaker's information

            Conference conference = new Conference();
            conference.setServerUrl(conferenceDataMap.getString("serverUrl"));
            conference.setCountryCode(conferenceDataMap.getString("countryCode"));
            conference.setTitle(conferenceDataMap.getString("title"));

            conferencesList.add(conference);
        }

        return conferencesList;

    }

}
