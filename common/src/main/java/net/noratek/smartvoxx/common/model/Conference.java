package net.noratek.smartvoxx.common.model;

/**
 * Created by eloudsa on 13/11/15.
 */
public class Conference {

    private String countryCode;
    private String title;
    private String name;
    private String serverUrl;

    public Conference(String countryCode, String title, String name, String serverUrl) {
        this.countryCode = countryCode;
        this.title = title;
        this.name = name;
        this.serverUrl = serverUrl;
    }

    public Conference(String countryCode, String title) {
        this.countryCode = countryCode;
        this.title = title;
    }

    public Conference() {
    }


    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
