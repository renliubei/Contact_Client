package com.example.contact_client.project_manager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class fragmentPageAdapter extends FragmentStateAdapter {

    private final int NUM_PAGES = 2;

    public fragmentPageAdapter(@NonNull FragmentActivity fragmentActivity,onLongClickGalleryImage onLongClickGalleryImage) {
        super(fragmentActivity);
        this.onLongClickGalleryImage = onLongClickGalleryImage;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0) return new ProjectGalleryFragment(onLongClickGalleryImage);
        return new ProjectEditorFragment();
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }

    public interface onLongClickGalleryImage{void onLongClick();}

    private onLongClickGalleryImage onLongClickGalleryImage;

}
