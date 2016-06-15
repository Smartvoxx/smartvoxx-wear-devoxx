package net.noratek.smartvoxxwear.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.view.FragmentGridPagerAdapter;

import net.noratek.smartvoxx.common.utils.Constants;
import net.noratek.smartvoxxwear.fragment.ScheduleFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by eloudsa on 23/08/15.
 */
public class ScheduleGridPageAdapter extends FragmentGridPagerAdapter {

    private final static String TAG = ScheduleGridPageAdapter.class.getCanonicalName();

    private static int NO_BACKGROUND = 0;

    private final Context mContext;


    // Pages of the GridViewPager
    private ArrayList<SimpleRow> mPages;

    // information provided by the activity
    private String mTitle;

    // fragments
    ScheduleFragment mScheduleFragment;

    HashMap<String, Fragment> mFragments = new HashMap<>();


    public ScheduleGridPageAdapter(Context context, FragmentManager fm, String title) {
        super(fm);

        mContext = context;
        mTitle = title;

        initPages();
    }

    private void initPages() {
        mPages = new ArrayList();

        SimpleRow row = new SimpleRow();

        row.addPages(new SimplePage(Constants.PAGER_SCHEDULES_LIST, mTitle, NO_BACKGROUND));
        mPages.add(row);
    }



    // Returns the Fragment related to the position in the grid

    @Override
    public Fragment getFragment(int row, int col) {

        SimplePage page = mPages.get(row).getPages(col);

        String pageId = page.getPageId() == null ? page.getPageName() : page.getPageId();

        Bundle bundle = new Bundle();

        // attach the fragment related to the position

        if (page.getPageName().equalsIgnoreCase(Constants.PAGER_SCHEDULES_LIST)) {
            // details

            mScheduleFragment = new ScheduleFragment();

            bundle.putString(Constants.DATAMAP_TITLE, page.getTitle());
            mScheduleFragment.setArguments(bundle);

            mFragments.put(pageId, mScheduleFragment);

            return mScheduleFragment;

        }

        return null;
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


}
