package com.example.contact_client.project_manager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class fragmentPageAdapter extends FragmentStateAdapter {

    private final int NUM_PAGES = 2;

    private ProjectGalleryFragment.onLongClickGalleryImage onLongClickGalleryImage;

    private ProjectEditorFragment.onClickItem onClickItem;

    public fragmentPageAdapter(
            @NonNull FragmentActivity fragmentActivity,
            ProjectGalleryFragment.onLongClickGalleryImage onLongClickGalleryImage,
            ProjectEditorFragment.onClickItem onClickItem) {
        super(fragmentActivity);
        this.onLongClickGalleryImage = onLongClickGalleryImage;
        this.onClickItem = onClickItem;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0) return ProjectGalleryFragment.newInstance(onLongClickGalleryImage);
        return ProjectEditorFragment.newInstance(onClickItem);
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }


}
