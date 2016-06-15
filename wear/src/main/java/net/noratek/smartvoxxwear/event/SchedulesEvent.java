package net.noratek.smartvoxxwear.event;

import net.noratek.smartvoxx.common.model.Schedule;

import java.util.List;

/**
 * Created by eloudsa on 16/06/16.
 */
public class SchedulesEvent {

    private List<Schedule> schedulesList;

    public SchedulesEvent(List<Schedule> schedulesList) {
        this.schedulesList = schedulesList;
    }

    public List<Schedule> getSchedulesList() {
        return schedulesList;
    }

    public void setSchedulesList(List<Schedule> schedulesList) {
        this.schedulesList = schedulesList;
    }
}
