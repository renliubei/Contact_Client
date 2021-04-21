package com.example.contact_client.project_manager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.contact_client.R;
import com.example.contact_client.databinding.ActivityVideoProjectBinding;
import com.example.contact_client.pageTransfomers.DepthPageTransformer;
import com.google.android.material.tabs.TabLayoutMediator;

import org.devio.takephoto.model.TImage;

import es.dmoral.toasty.Toasty;

import static com.example.contact_client.project_manager.ProjectEditorFragment.SEARCH_PHOTO;

public class VideoProjectActivity extends AppCompatActivity {
    protected static final String IMAGE = "image";
    private ProjectViewModel mViewModel;
    private ActivityVideoProjectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_project);
        binding.setLifecycleOwner(this);
        mViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);

        fragmentPageAdapter adapter = new fragmentPageAdapter(this, () -> binding.viewPage2ProjectGallery.setCurrentItem(1, true), null);
        binding.viewPage2ProjectGallery.setAdapter(adapter);
        binding.viewPage2ProjectGallery.setPageTransformer(new DepthPageTransformer());
        binding.viewPage2ProjectGallery.setUserInputEnabled(false);
        TabLayoutMediator mediator = new TabLayoutMediator(binding.tabLayout2, binding.viewPage2ProjectGallery, (tab, position) -> {
            if (position == 0) {
                tab.setIcon(R.drawable.ic_baseline_menu_book_64_white);
            }
            if (position == 1) {
                tab.setIcon(R.drawable.ic_baseline_edit_48_white);
            }
        });
        mediator.attach();
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SEARCH_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        TImage image = (TImage) data.getSerializableExtra(IMAGE);
                        Log.d("mylo","receive image data from local:"+image.getOriginalPath());
                        mViewModel.getHintCover().setValue(image.getOriginalPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toasty.error(this, "更新头像失败", Toasty.LENGTH_SHORT).show();
                    }
                }
        }
    }
}