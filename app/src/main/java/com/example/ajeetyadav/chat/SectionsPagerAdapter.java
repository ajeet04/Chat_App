package com.example.ajeetyadav.chat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Ajeet Yadav on 9/10/2017.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                RequestFragment rf=new RequestFragment();
                return rf;

            case 1:
                ChatFragment cf=new ChatFragment();
                return cf;
            case 2:
                Freinds ff=new Freinds();
                return ff;
      default:
          return null;

        }

    }

    @Override
    public int getCount() {
        return 3;
    }
   public  CharSequence getPageTitle(int pos){
       switch(pos) {
           case 0:
               return "REQUEST";
           case 1:
               return "CHAT";
           case 2:
               return "FRIEND LIST";
           default:
               return null;
       }
   }


}
