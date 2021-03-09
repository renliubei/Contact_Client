package com.example.contact_client.project_manager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.contact_client.R;
import com.example.contact_client.databinding.ActivityVideoProjectBinding;
import com.example.contact_client.project_manager.pageTransfomers.DepthPageTransformer;
import com.google.android.material.tabs.TabLayoutMediator;

public class VideoProjectActivity extends AppCompatActivity {

    private ProjectViewModel mViewModel;
    private ActivityVideoProjectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_video_project);
        binding.setLifecycleOwner(this);
        mViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);

        fragmentPageAdapter adapter = new fragmentPageAdapter(this, new fragmentPageAdapter.onLongClickGalleryImage() {
            @Override
            public void onLongClick() {
                binding.viewPage2ProjectGallery.setCurrentItem(1,true);
            }
        });

        binding.viewPage2ProjectGallery.setAdapter(adapter);
        binding.viewPage2ProjectGallery.setPageTransformer( new DepthPageTransformer());
        binding.viewPage2ProjectGallery.setUserInputEnabled(false);
        TabLayoutMediator mediator = new TabLayoutMediator(binding.tabLayout2, binding.viewPage2ProjectGallery, (tab, position) -> {
            if(position==0){
                tab.setIcon(R.drawable.ic_baseline_menu_book_64_white);
            }
            if(position==1) {
                tab.setIcon(R.drawable.ic_baseline_edit_48_white);
            }
        });
        mediator.attach();
    }

    @Override
    public void onBackPressed() {
        if (binding.viewPage2ProjectGallery.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            binding.viewPage2ProjectGallery.setCurrentItem(binding.viewPage2ProjectGallery.getCurrentItem() - 1);
        }
    }

}