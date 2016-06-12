package net.noratek.smartvoxxwear.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.view.FragmentGridPagerAdapter;

import net.noratek.smartvoxx.common.model.Speaker;
import net.noratek.smartvoxx.common.utils.Constants;
import net.noratek.smartvoxxwear.fragment.TalkFragment;
import net.noratek.smartvoxxwear.fragment.TalkSpeakerFragment;
import net.noratek.smartvoxxwear.fragment.TalkSummaryFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by eloudsa on 23/08/15.
 */
public class TalkGridPageAdapter extends FragmentGridPagerAdapter {

    private final static String TAG = TalkGridPageAdapter.class.getCanonicalName();

    private static int NO_BACKGROUND = 0;


    private final Context mContext;


    // Pages of the GridViewPager
    private ArrayList<SimpleRow> mPages;

    // Speakers: row used to store each speaker
    private SimpleRow mRowSpeakers;

    // information provided by the Talk activity
    private String mTalkTitle;

    // fragments
    TalkFragment mTalkFragment;
    TalkSummaryFragment mTalkSummaryFragment;
    TalkSpeakerFragment mTalkSpeakerFragment;

    HashMap<String, Fragment> mFragments = new HashMap<>();


    public TalkGridPageAdapter(Context context, FragmentManager fm, String talkTitle) {
        super(fm);

        mContext = context;
        mTalkTitle = talkTitle;

        initPages();
    }

    private void initPages() {
        mPages = new ArrayList();

        SimpleRow row = new SimpleRow();

        row.addPages(new SimplePage(Constants.PAGER_TALK_INFO, mTalkTitle, NO_BACKGROUND));
        row.addPages(new SimplePage(Constants.PAGER_TALK_SUMMARY, mTalkTitle, NO_BACKGROUND));
        mPages.add(row);

        mRowSpeakers = new SimpleRow();
    }



    // Returns the Fragment related to the position in the grid

    @Override
    public Fragment getFragment(int row, int col) {

        SimplePage page = mPages.get(row).getPages(col);

        String pageId = page.getPageId() == null ? page.getPageName() : page.getPageId();

        Bundle bundle = new Bundle();

        // attach the fragment related to the position

        if (page.getPageName().equalsIgnoreCase(Constants.PAGER_TALK_INFO)) {
            // Talk details

            mTalkFragment = new TalkFragment();

            bundle.putString(Constants.DATAMAP_TITLE, page.getTitle());
            mTalkFragment.setArguments(bundle);

            mFragments.put(pageId, mTalkFragment);

            return mTalkFragment;

        } else if (page.getPageName().equalsIgnoreCase(Constants.PAGER_TALK_SUMMARY)) {
            // Talk summary

            mTalkSummaryFragment = new TalkSummaryFragment();
            mFragments.put(pageId, mTalkSummaryFragment);

            return mTalkSummaryFragment;

        }

        // Speaker

        // default is a card for the speaker(s)
        mTalkSpeakerFragment = new TalkSpeakerFragment();

        bundle.putString(Constants.DATAMAP_SPEAKER_ID, page.getPageId());
        mTalkSpeakerFragment.setArguments(bundle);

        mFragments.put(pageId, mTalkSpeakerFragment);

        return mTalkSpeakerFragment;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Drawable getBackgroundForPage(int row, int col) {
        // getDrawable() is deprecated since API level 22.

        SimplePage page = mPages.get(row).getPages(col);

        if (page.getBackgroundId() == NO_BACKGROUND) {
            // no background
            return BACKGROUND_NONE;
        }

        Drawable drawable = mContext.getResources().getDrawable(page.getBackgroundId());

        return drawable;
    }

    @Override
    public int getRowCount() {
        return mPages.size();
    }

    @Override
    public int getColumnCount(int row) {
        return mPages.get(row).size();
    }


    // add dynamically incoming speakers on the row
    public void addSpeakers(LinkedHashMap<String, Speaker> speakersList) {

        if (speakersList == null) {
            return;
        }

        if (speakersList.size() == 0) {
            return;
        }

        for (Speaker speaker : speakersList.values()) {
            mRowSpeakers.addPages(new SimplePage(speaker.getUuid(), Constants.PAGER_TALK_SPEAKER, Constants.PAGER_SPEAKER, NO_BACKGROUND));
        }
        mPages.add(mRowSpeakers);
    }

}
