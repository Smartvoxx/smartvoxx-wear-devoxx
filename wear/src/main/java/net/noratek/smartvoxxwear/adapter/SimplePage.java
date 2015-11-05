package net.noratek.smartvoxxwear.adapter;

import java.io.Serializable;

/**
 * Created by eloudsa on 24/08/15.
 */
public class SimplePage implements Serializable {

    private static final long serialVersionUID = -4816792608001679309L;

    private String mPageId;
    private String mPageName;
    private String mTitle;
    private int mBackgroundId;


    public SimplePage(String pageId, String pageName, String title, int backgroundId) {
        this.mPageId = pageId;
        this.mPageName = pageName;
        this.mTitle = title;
        this.mBackgroundId = backgroundId;
    }

    public SimplePage(String pageName, String title, int backgroundId) {
        this.mPageName = pageName;
        this.mTitle = title;
        this.mBackgroundId = backgroundId;
    }

    public String getPageId() {
        return mPageId;
    }

    public void setPageId(String mPageId) {
        this.mPageId = mPageId;
    }

    public String getPageName() {
        return mPageName;
    }

    public void setPageName(String pageName) {
        this.mPageName = pageName;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getBackgroundId() {
        return mBackgroundId;
    }

    public void setBackgroundId(int mBackgroundId) {
        this.mBackgroundId = mBackgroundId;
    }
}
