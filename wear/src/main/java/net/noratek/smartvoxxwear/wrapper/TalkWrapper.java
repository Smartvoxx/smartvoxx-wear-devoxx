package net.noratek.smartvoxxwear.wrapper;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;

import net.noratek.smartvoxx.common.model.Speaker;
import net.noratek.smartvoxx.common.model.Talk;
import net.noratek.smartvoxx.common.utils.Constants;

import java.util.List;

/**
 * Created by eloudsa on 08/09/15.
 */
public class TalkWrapper {

    public Talk getTalk(DataEvent dataEvent) {


        if (dataEvent == null) {
            return null;
        }

        DataMapItem dataMapItem = DataMapItem.fromDataItem(dataEvent.getDataItem());
        if (dataMapItem == null) {
            return null;
        }

        return getTalk(dataMapItem.getDataMap());
    }


    public Talk getTalk(DataMap dataMap) {

        if (dataMap == null) {
            return null;
        }

        DataMap dataTalkMap = dataMap.getDataMap(Constants.DETAIL_PATH);
        if (dataTalkMap == null) {
            return null;
        }

        Talk talk = new Talk();

        talk.setId(dataTalkMap.getString(Constants.DATAMAP_ID, ""));
        talk.setEventId(dataTalkMap.getLong(Constants.DATAMAP_EVENT_ID, 0L));
        talk.setTalkType(dataTalkMap.getString(Constants.DATAMAP_TALK_TYPE, ""));
        talk.setTrack(dataTalkMap.getString(Constants.DATAMAP_TRACK, ""));
        talk.setTrackId(dataTalkMap.getString(Constants.DATAMAP_TRACK_ID, ""));
        talk.setTitle(dataTalkMap.getString(Constants.DATAMAP_TITLE, ""));
        talk.setLang(dataTalkMap.getString(Constants.DATAMAP_LANG, ""));
        talk.setSummary(dataTalkMap.getString(Constants.DATAMAP_SUMMARY, ""));


        List<DataMap> speakersDataMap = dataTalkMap.getDataMapArrayList(Constants.SPEAKERS_PATH);
        if (speakersDataMap == null) {
            return talk;
        }

        for (DataMap speakerDataMap : speakersDataMap) {
            // retrieve the speaker's information

            Speaker speaker = new Speaker();

            speaker.setUuid(speakerDataMap.getString(Constants.DATAMAP_UUID, ""));
            speaker.setFullName(speakerDataMap.getString(Constants.DATAMAP_TITLE, ""));


            talk.addSpeaker(speaker);
        }

        return talk;
    }

}
