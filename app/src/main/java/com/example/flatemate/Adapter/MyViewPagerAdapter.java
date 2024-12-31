package com.example.flatemate.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.flatemate.HomeFragment;
import com.example.flatemate.ImagesFragment;
import com.example.flatemate.MaintenenceFragment;
import com.example.flatemate.ProfileFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new ImagesFragment();
            case 2:
                return new MaintenenceFragment();
            case 3:
                return new ProfileFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4; // Number of tabs
    }
}
