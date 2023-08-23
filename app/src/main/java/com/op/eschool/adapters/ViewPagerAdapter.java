package com.op.eschool.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    List<Fragment> list = new ArrayList<>() ;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> list) {
        super(fragmentActivity);
        this.list = list;
    }
    @NotNull
    @Override
    public Fragment createFragment(int p) {
        return  list.get(p) ;
    }
    @Override
    public int getItemCount() {
        return list.size() ;
    }
}