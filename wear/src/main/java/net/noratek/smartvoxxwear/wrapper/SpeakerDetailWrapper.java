package net.noratek.smartvoxxwear.wrapper;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;

import net.noratek.smartvoxx.common.model.Speaker;
import net.noratek.smartvoxx.common.utils.Constants;

/**
 * Created by eloudsa on 29/08/15.
 */
public class SpeakerDetailWrapper {


    public Speaker getSpeakerDetail(DataEvent dataEvent) {


        if (dataEvent == null) {
            return null;
        }

        DataMapItem dataMapItem = DataMapItem.fromDataItem(dataEvent.getDataItem());
        if (dataMapItem == null) {
            return null;
        }


        return getSpeakerDetail(dataMapItem.getDataMap());
    }

    public Speaker getSpeakerDetail(DataMap dataMap) {

        DataMap speakerDataMap = dataMap.getDataMap(Constants.DETAIL_PATH);
        if (speakerDataMap == null) {
            return null;
        }

        // retrieve the speaker's information
        Speaker speaker = new Speaker(
                speakerDataMap.getString(Constants.DATAMAP_UUID, ""),
                speakerDataMap.getString(Constants.DATAMAP_LAST_NAME, ""),
                speakerDataMap.getString(Constants.DATAMAP_FIRST_NAME, ""),
                speakerDataMap.getString(Constants.DATAMAP_BLOG, ""),
                speakerDataMap.getString(Constants.DATAMAP_TWITTER, ""),
                speakerDataMap.getString(Constants.DATAMAP_COMPANY, ""),
                speakerDataMap.getString(Constants.DATAMAP_BIO, ""),
                speakerDataMap.getString(Constants.DATAMAP_AVATAR_URL, ""),
                speakerDataMap.getString(Constants.DATAMAP_AVATAR_IMAGE, ""));

        return speaker;


    }

}
