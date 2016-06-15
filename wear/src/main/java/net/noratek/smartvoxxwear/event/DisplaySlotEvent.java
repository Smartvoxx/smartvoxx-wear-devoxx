package net.noratek.smartvoxxwear.event;

/**
 * Created by eloudsa on 16/06/16.
 */
public class DisplaySlotEvent {

    private String dayName;

    public DisplaySlotEvent(String dayName) {
        this.dayName = dayName;
    }

    public DisplaySlotEvent() {
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }



}
