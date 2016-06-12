package net.noratek.smartvoxxwear.wrapper;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;

import net.noratek.smartvoxx.common.model.BreakSession;
import net.noratek.smartvoxx.common.model.Slot;
import net.noratek.smartvoxx.common.model.Talk;
import net.noratek.smartvoxx.common.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eloudsa on 03/09/15.
 */
public class SlotsWrapper {

    public List<Slot> getSlotsList(DataEvent dataEvent) {

        List<Slot> slotsList = new ArrayList<>();

        if (dataEvent == null) {
            return slotsList;
        }

        DataMapItem dataMapItem = DataMapItem.fromDataItem(dataEvent.getDataItem());
        if (dataMapItem == null) {
            return slotsList;
        }

        return getSlotsList(dataMapItem.getDataMap());
    }



    public List<Slot> getSlotsList(DataMap dataMap) {

        List<Slot> slotsList = new ArrayList<>();

        if (dataMap == null) {
            return slotsList;
        }

        List<DataMap> slotsDataMap = dataMap.getDataMapArrayList(Constants.LIST_PATH);
        if (slotsDataMap == null) {
            return slotsList;
        }

        for (DataMap slotDataMap : slotsDataMap) {
            // retrieve the speaker's information

            Slot slot = new Slot();


            slot.setRoomName(slotDataMap.getString(Constants.DATAMAP_ROOM_NAME, ""));
            slot.setFromTimeMillis(slotDataMap.getLong(Constants.DATAMAP_FROM_TIME_MILLIS, 0L));
            slot.setToTimeMillis(slotDataMap.getLong(Constants.DATAMAP_TO_TIME_MILLIS, 0L));


            DataMap breakDataMap = slotDataMap.getDataMap(Constants.DATAMAP_BREAK);
            if (breakDataMap != null) {
                BreakSession breakSlot = new BreakSession();

                breakSlot.setId(breakDataMap.getString(Constants.DATAMAP_ID, ""));
                breakSlot.setNameEN(breakDataMap.getString(Constants.DATAMAP_NAME_EN, ""));
                breakSlot.setNameFR(breakDataMap.getString(Constants.DATAMAP_NAME_FR, ""));

                slot.setBreakSession(breakSlot);
            }

            DataMap talkDataMap = slotDataMap.getDataMap(Constants.DATAMAP_TALK);
            if (talkDataMap != null) {
                Talk talkSlot = new Talk();

                talkSlot.setId(talkDataMap.getString(Constants.DATAMAP_ID, ""));
                talkSlot.setEventId(talkDataMap.getLong(Constants.DATAMAP_EVENT_ID, 0L));
                talkSlot.setLang(talkDataMap.getString(Constants.DATAMAP_LANG, ""));
                talkSlot.setSummary(talkDataMap.getString(Constants.DATAMAP_SUMMARY, ""));
                talkSlot.setTalkType(talkDataMap.getString(Constants.DATAMAP_TALK_TYPE, ""));
                talkSlot.setTitle(talkDataMap.getString(Constants.DATAMAP_TITLE, ""));
                talkSlot.setTrack(talkDataMap.getString(Constants.DATAMAP_TRACK, ""));
                talkSlot.setTrackId(talkDataMap.getString(Constants.DATAMAP_TRACK_ID, ""));

                slot.setTalk(talkSlot);
            }

            // skip unknown talks
            if ((slot.getBreakSession() != null) || (slot.getTalk() != null)) {
                slotsList.add(slot);
            }


        }

        return slotsList;

    }

}
