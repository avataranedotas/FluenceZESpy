package pt.alexmol.fluencezespy;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * A FragmentPagerAdapter that returns a fragment corresponding to one of
 * the primary sections of the app.
 */
class MyPagerAdapter extends FragmentPagerAdapter {

    private final MainActivity myPagerAdapter;
    private final SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public MyPagerAdapter(MainActivity MyPagerAdapter, Context c, FragmentManager fm) {
        super(fm);
        myPagerAdapter = MyPagerAdapter;
        Context c1 = c;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        if (i == 0) {
            fragment = new Page0();
        }
        if (i == 1) {
            fragment = new Page1();
        }
        if (i == 2) {
            fragment = new Page2();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return myPagerAdapter.getString(R.string.title_section0).toUpperCase();
            case 1:
                return myPagerAdapter.getString(R.string.title_section1).toUpperCase();
            case 2:
                return myPagerAdapter.getString(R.string.title_section2).toUpperCase();
        }
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }


}
