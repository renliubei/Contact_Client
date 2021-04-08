package com.example.contact_client;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.contact_client.databinding.FactoryFragmentBinding;
import com.example.contact_client.project_creator.MainActivity;
import com.example.contact_client.project_manager.VideoProjectActivity;
import com.example.contact_client.video_manager.VideoManagerActivity;
import com.ramotion.circlemenu.CircleMenuView;

public class FactoryFragment extends Fragment {
    private FactoryFragmentBinding factoryFragmentBinding;
    private FactoryViewModel mViewModel;
    public static FactoryFragment newInstance() {
        return new FactoryFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        factoryFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.factory_fragment, container, false);
        factoryFragmentBinding.setLifecycleOwner(getActivity());
        factoryFragmentBinding.circleMenuMain.setEventListener(new CircleMenuView.EventListener(){
            @Override
            public void onMenuOpenAnimationEnd(@NonNull CircleMenuView view) {
                super.onMenuOpenAnimationEnd(view);
                showFadingText();
                hideHeadText();
            }
            @Override
            public void onMenuCloseAnimationEnd(@NonNull CircleMenuView view) {
                super.onMenuCloseAnimationEnd(view);
                hideFadingText();
                showHeadText();
            }
            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int buttonIndex) {
                super.onButtonClickAnimationStart(view, buttonIndex);
                hideFadingText();
                showHeadText();
            }
            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int buttonIndex) {
                super.onButtonClickAnimationEnd(view, buttonIndex);
                Intent intent;
                switch (buttonIndex){
                    case 0:
                        intent = new Intent(getActivity(), MainActivity.class);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), VideoProjectActivity.class);
                        break;
                    default:
                        intent = new Intent(getActivity(), VideoManagerActivity.class);
                }
                startActivity(intent);
            }
        });
        return factoryFragmentBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        hideFadingText();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FactoryViewModel.class);
        // TODO: Use the ViewModel
    }

    void hideFadingText(){
        factoryFragmentBinding.fadingTextPlay.stop();
        factoryFragmentBinding.fadingTextResource.stop();
        factoryFragmentBinding.fadingTextCreate.stop();
        factoryFragmentBinding.fadingTextPlay.setVisibility(View.GONE);
        factoryFragmentBinding.fadingTextCreate.setVisibility(View.GONE);
        factoryFragmentBinding.fadingTextResource.setVisibility(View.GONE);
    }

    void showFadingText(){
        factoryFragmentBinding.fadingTextPlay.restart();
        factoryFragmentBinding.fadingTextResource.restart();
        factoryFragmentBinding.fadingTextCreate.restart();
        factoryFragmentBinding.fadingTextPlay.setVisibility(View.VISIBLE);
        factoryFragmentBinding.fadingTextCreate.setVisibility(View.VISIBLE);
        factoryFragmentBinding.fadingTextResource.setVisibility(View.VISIBLE);
    }

    void showHeadText(){
        factoryFragmentBinding.textView.setAnimation(AnimationUtils.makeInAnimation(getActivity(),true));
        factoryFragmentBinding.textView.setVisibility(View.VISIBLE);
    }

    void hideHeadText(){
        factoryFragmentBinding.textView.setAnimation(AnimationUtils.makeOutAnimation(getActivity(),true));
        factoryFragmentBinding.textView.setVisibility(View.INVISIBLE);
    }
}