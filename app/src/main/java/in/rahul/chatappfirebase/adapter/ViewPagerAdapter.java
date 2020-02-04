package in.rahul.chatappfirebase.adapter;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import in.rahul.chatappfirebase.fragment.FriendReqFragment;
import in.rahul.chatappfirebase.fragment.HomeFragment;
import in.rahul.chatappfirebase.fragment.InvitationFragment;

/**
 * Created by Rahul on 13-08-2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0){
            fragment = new HomeFragment();
        } else if (position == 1){
            fragment = new FriendReqFragment();
        } else if (position == 2){
            fragment = new InvitationFragment();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position){
        String title = null;
        if (position == 0){
            title = "Home";
        } else if (position == 1){
            title = "Friend Request";
        } else if (position == 2){
            title = "Invitation";
        }
        return title;
    }
}
