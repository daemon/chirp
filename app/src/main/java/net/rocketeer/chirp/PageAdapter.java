package net.rocketeer.chirp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    private int numberoftabs;

    public PageAdapter(FragmentManager fm, int numberoftabs) {
        super(fm);
        this.numberoftabs = numberoftabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                return new tab1();
            case 1:
                return new tab2();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return numberoftabs;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
