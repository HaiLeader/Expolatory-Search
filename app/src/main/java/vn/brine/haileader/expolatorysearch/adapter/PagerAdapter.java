package vn.brine.haileader.expolatorysearch.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vn.brine.haileader.expolatorysearch.fragments.MovieFragment;
import vn.brine.haileader.expolatorysearch.fragments.MusicFragment;

/**
 * Created by HaiLeader on 7/19/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MovieFragment tab1 = new MovieFragment();
                return tab1;
            case 1:
                MusicFragment tab2 = new MusicFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
