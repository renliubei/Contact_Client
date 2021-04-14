package com.example.contact_client.project_creator.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.contact_client.project_creator.CreatorViewModel;
import com.example.contact_client.project_creator.fragments.ConditionFragment;
import com.example.contact_client.project_creator.fragments.CreatorListFragment;

public class FragmentAdapter extends FragmentStateAdapter {
    CreatorViewModel viewModel;
    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity,CreatorViewModel viewModel) {
        super(fragmentActivity);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0)
            return CreatorListFragment.newInstance(viewModel);
        else
            return ConditionFragment.newInstance(viewModel);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
