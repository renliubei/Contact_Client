package com.example.contact_client.project_manager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.contact_client.R;
import com.example.contact_client.databinding.ActivityVideoProjectBinding;
import com.example.contact_client.project_manager.pageTransfomers.DepthPageTransformer;
import com.google.android.material.tabs.TabLayout;
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
        fragmentPageAdapter adapter = new fragmentPageAdapter(this);
        binding.viewPage2ProjectGallery.setAdapter(adapter);
        binding.viewPage2ProjectGallery.setPageTransformer(new DepthPageTransformer());
        binding.viewPage2ProjectGallery.setUserInputEnabled(false);
        TabLayoutMediator mediator = new TabLayoutMediator(binding.tabLayout2, binding.viewPage2ProjectGallery, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if(position==0) tab.setText("画廊");
                if(position==1) tab.setText("详情");
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