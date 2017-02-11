package br.com.arthurgrangeiro.saopaulo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Arthur on 07/01/2017.
 */

public class SectionAdapter extends FragmentPagerAdapter {

    public SectionAdapter(FragmentManager fm) {
        super(fm);
    }

    String museum = MainActivity.context.getString(R.string.section_name_museums);
    String park = MainActivity.context.getString(R.string.section_name_parks);
    String site = MainActivity.context.getString(R.string.section_name_sites);
    String bar = MainActivity.context.getString(R.string.section_name_bars);

    //Vector with the tab titles
    private String tabTitles[] = new String[]{museum, park, site, bar};


    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MuseumsFragment();
        } else if (position == 1) {
            return new ParksFragment();
        } else if (position == 2) {
            return new SitesFragment();
        } else {
            return new BarsFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
