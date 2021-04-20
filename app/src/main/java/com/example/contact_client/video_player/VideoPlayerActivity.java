package com.example.contact_client.video_player;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.contact_client.R;
import com.example.contact_client.project_creator.VideoNode;
import com.example.contact_client.repository.VideoProject;
import com.example.contact_client.repository.mRepository;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class VideoPlayerActivity extends AppCompatActivity {
    private static final int ROOT_NODE = -1;
    private VideoNode currentNode;
    private List<VideoNode> sonNodes=new ArrayList<>();
    private VideoProject mVideoProject;
    private MediaItem mediaItem;
    private  SimpleExoPlayer player;
    private mRepository mRepository;
    private PlayerView playerView;
    private View.OnClickListener onClickOptionBtn;
    ConstraintSet constraintSet = new ConstraintSet();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    Button leftUpBtn,leftDownBtn,rightUpBtn,rightDownBtn,restart,quit;
    TextView narrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        mRepository = new mRepository(this);
        playerView = findViewById(R.id.videoPlayerView);
        narrator = findViewById(R.id.playerPlotNarrator);
        getProject();
        initPlayer();
        bindBtn();
        playProject();
    }
    void playProject(){
        currentNode = mVideoProject.getVideoNodeList().get(0);
        player.clearMediaItems();
        playCurrentNode();
    }

    private List<VideoNode> filterSons(){
        return mVideoProject.filterSons(currentNode);
    }

    void playCurrentNode(){
        mVideoProject.changeConditions(currentNode);
        sonNodes = mVideoProject.filterSons(currentNode);
        if(sonNodes==null)
            finish();
        if(currentNode.getId()==ROOT_NODE){
            playerView.hideController();
            showBtn();
            showPlot();
        }else{
            mDisposable.add(
                    mRepository.findVideoCutById(currentNode.getId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(videoCut -> {
                        mediaItem = MediaItem.fromUri(videoCut.getUrlString());
                        player.setMediaItem(mediaItem);
                        player.prepare();
                        playerView.setClickable(true);
                    },throwable -> {throwable.printStackTrace();})
            );
        }
    }

    void getProject(){
        mVideoProject = getIntent().getParcelableExtra(getString(R.string.videoProject));
        if(mVideoProject==null){
            Toasty.error(getApplicationContext(),"未读取到互动视频",Toasty.LENGTH_SHORT,true).show();
            finish();
        }
    }
    void initPlayer(){
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                error.printStackTrace();
            }
        });
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                String stateString;
                switch (state) {
                    case Player.STATE_IDLE:
                        stateString = "ExoPlayer.STATE_IDLE      -";
                        break;
                    case Player.STATE_BUFFERING:
                        stateString = "ExoPlayer.STATE_BUFFERING -";
                        break;
                    case Player.STATE_READY:
                        stateString = "ExoPlayer.STATE_READY     -";
                        break;
                    case Player.STATE_ENDED:
                        stateString = "ExoPlayer.STATE_ENDED     -";
                        playerView.hideController();
                        showBtn();
                        showPlot();
                        break;
                    default:
                        stateString = "UNKNOWN_STATE             -";
                        break;
                }
                Log.d("mylo",stateString);
            }
        });

        player.addListener(new Player.EventListener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if(isPlaying){
                    hidePlot();
                    hideAllBtn();
                }
            }
        });

        player.setPlayWhenReady(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }

    private void relocateBtn(){
        int MARGIN = 20;
        int btnNum=sonNodes.size();
        constraintSet.clone(this,R.layout.activity_video_player);
        switch (btnNum){
            case 0:
                Log.d("mylo","on relocate buttons: no sons");
            case 1:
                constraintSet.clear(R.id.leftUpBtn);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START,MARGIN);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END,MARGIN);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.TOP,R.id.guideline_player_60h,ConstraintSet.TOP,MARGIN);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM,MARGIN);
                break;
            case 2:
                constraintSet.clear(R.id.leftUpBtn);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START,MARGIN);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END,MARGIN);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.TOP,R.id.guideline_player_60h,ConstraintSet.TOP);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.BOTTOM,R.id.guideline_player_80h,ConstraintSet.BOTTOM);

                constraintSet.clear(R.id.rightUpBtn);
                constraintSet.connect(R.id.rightUpBtn,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START,MARGIN);
                constraintSet.connect(R.id.rightUpBtn,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END,MARGIN);
                constraintSet.connect(R.id.rightUpBtn,ConstraintSet.TOP,R.id.guideline_player_80h,ConstraintSet.TOP);
                constraintSet.connect(R.id.rightUpBtn,ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM);
                break;
            case 3:
                constraintSet.clear(R.id.leftUpBtn);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.TOP,R.id.guideline_player_60h,ConstraintSet.TOP);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.BOTTOM,R.id.guideline_player_80h,ConstraintSet.BOTTOM);

                constraintSet.clear(R.id.rightUpBtn);
                constraintSet.connect(R.id.rightUpBtn,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START);
                constraintSet.connect(R.id.rightUpBtn,ConstraintSet.END,R.id.guideline_player_50v,ConstraintSet.END);
                constraintSet.connect(R.id.rightUpBtn,ConstraintSet.TOP,R.id.guideline_player_80h,ConstraintSet.TOP);
                constraintSet.connect(R.id.rightUpBtn,ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM);

                constraintSet.clear(R.id.leftDownBtn);
                constraintSet.connect(R.id.leftDownBtn,ConstraintSet.START,R.id.guideline_player_50v,ConstraintSet.START);
                constraintSet.connect(R.id.leftDownBtn,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END);
                constraintSet.connect(R.id.leftDownBtn,ConstraintSet.TOP,R.id.guideline_player_80h,ConstraintSet.TOP);
                constraintSet.connect(R.id.leftDownBtn,ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM);
                break;
            case 4:
                constraintSet.clear(R.id.leftUpBtn);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.END,R.id.guideline_player_50v,ConstraintSet.END);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.TOP,R.id.guideline_player_60h,ConstraintSet.TOP);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.BOTTOM,R.id.guideline_player_80h,ConstraintSet.BOTTOM);

                constraintSet.clear(R.id.rightUpBtn);
                constraintSet.connect(R.id.rightUpBtn,ConstraintSet.START,R.id.guideline_player_50v,ConstraintSet.START);
                constraintSet.connect(R.id.rightUpBtn,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END);
                constraintSet.connect(R.id.rightUpBtn,ConstraintSet.TOP,R.id.guideline_player_60h,ConstraintSet.TOP);
                constraintSet.connect(R.id.rightUpBtn,ConstraintSet.BOTTOM,R.id.guideline_player_80h,ConstraintSet.BOTTOM);

                constraintSet.clear(R.id.leftDownBtn);
                constraintSet.connect(R.id.leftDownBtn,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START);
                constraintSet.connect(R.id.leftDownBtn,ConstraintSet.END,R.id.guideline_player_50v,ConstraintSet.END);
                constraintSet.connect(R.id.leftDownBtn,ConstraintSet.TOP,R.id.guideline_player_80h,ConstraintSet.TOP);
                constraintSet.connect(R.id.leftDownBtn,ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM);

                constraintSet.clear(R.id.rightDownBtn);
                constraintSet.connect(R.id.rightDownBtn,ConstraintSet.START,R.id.guideline_player_50v,ConstraintSet.START);
                constraintSet.connect(R.id.rightDownBtn,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END);
                constraintSet.connect(R.id.rightDownBtn,ConstraintSet.TOP,R.id.guideline_player_80h,ConstraintSet.TOP);
                constraintSet.connect(R.id.rightDownBtn,ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM);
                break;
        }

        constraintSet.constrainWidth(R.id.rightUpBtn,ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(R.id.rightUpBtn,ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(R.id.leftUpBtn,ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(R.id.leftUpBtn,ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(R.id.rightDownBtn,ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(R.id.rightDownBtn,ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(R.id.leftDownBtn,ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(R.id.leftDownBtn,ConstraintSet.WRAP_CONTENT);

        ConstraintLayout constraintLayout = getWindow().getDecorView().findViewById(R.id.playerLayout);
        constraintSet.applyTo(constraintLayout);
    }

    private void bindBtn(){
        leftDownBtn  = findViewById(R.id.leftDownBtn);
        rightDownBtn  = findViewById(R.id.rightDownBtn);
        leftUpBtn  = findViewById(R.id.leftUpBtn);
        rightUpBtn  = findViewById(R.id.rightUpBtn);
        restart = findViewById(R.id.buttonRestart);
        quit = findViewById(R.id.buttonQuit);

        onClickOptionBtn = v -> {
                if(v==leftUpBtn){
                    currentNode = sonNodes.get(0);
                }else if(v==rightUpBtn){
                    currentNode = sonNodes.get(1);
                }else if(v==leftDownBtn){
                    currentNode = sonNodes.get(2);
                }else if(v==rightDownBtn){
                    currentNode = sonNodes.get(3);
                }
                hidePlot();
                hideAllBtn();
                playCurrentNode();
        };

        leftUpBtn.setOnClickListener(onClickOptionBtn);
        rightDownBtn.setOnClickListener(onClickOptionBtn);
        rightUpBtn.setOnClickListener(onClickOptionBtn);
        leftDownBtn.setOnClickListener(onClickOptionBtn);
        restart.setOnClickListener(v -> playProject());
        quit.setOnClickListener(v->finish());
    }

    private void setOptionBtnVisible(){
        int showNum=sonNodes.size();
        if(showNum>=1){
            leftUpBtn.setText(sonNodes.get(0).getBtnText());
            leftUpBtn.setAnimation(AnimationUtils.makeInAnimation(this,true));
            leftUpBtn.setVisibility(View.VISIBLE);
        }
        if(showNum>=2){
            rightUpBtn.setText(sonNodes.get(1).getBtnText());
            rightUpBtn.setAnimation(AnimationUtils.makeInAnimation(this,true));
            rightUpBtn.setVisibility(View.VISIBLE);
        }
        if(showNum>=3){
            leftDownBtn.setText(sonNodes.get(2).getBtnText());
            leftDownBtn.setAnimation(AnimationUtils.makeInAnimation(this,true));
            leftDownBtn.setVisibility(View.VISIBLE);
        }
        if(showNum>=4){
            rightDownBtn.setText(sonNodes.get(3).getBtnText());
            rightDownBtn.setAnimation(AnimationUtils.makeInAnimation(this,true));
            rightDownBtn.setVisibility(View.VISIBLE);
        }
    }

    private void showBtn(){
        playerView.setClickable(false);
        int sonsNum=sonNodes.size();
        if(sonsNum==0)
            showEndBtn();
        else{
            relocateBtn();
            setOptionBtnVisible();
        }
    }

    private void showEndBtn(){
        quit.startAnimation(AnimationUtils.makeInAnimation(this,true));
        quit.setVisibility(View.VISIBLE);

        restart.startAnimation(AnimationUtils.makeInAnimation(this,true));
        restart.setVisibility(View.VISIBLE);
    }

    private void hideAllBtn(){
        if(leftUpBtn.getVisibility()==View.VISIBLE){
            leftUpBtn.setVisibility(View.GONE);
            leftUpBtn.startAnimation(AnimationUtils.makeOutAnimation(this,true));
        }
        if(leftDownBtn.getVisibility()==View.VISIBLE){
            leftDownBtn.setVisibility(View.GONE);
            leftDownBtn.startAnimation(AnimationUtils.makeOutAnimation(this,true));
        }
        if(rightDownBtn.getVisibility()==View.VISIBLE){
            rightDownBtn.setVisibility(View.GONE);
            rightDownBtn.startAnimation(AnimationUtils.makeOutAnimation(this,true));
        }
        if(rightUpBtn.getVisibility()==View.VISIBLE){
            rightUpBtn.setVisibility(View.GONE);
            rightUpBtn.startAnimation(AnimationUtils.makeOutAnimation(this,true));
        }
        if(restart.getVisibility()==View.VISIBLE){
            restart.setVisibility(View.GONE);
            restart.startAnimation(AnimationUtils.makeOutAnimation(this,true));
        }
        if(quit.getVisibility()==View.VISIBLE){
            quit.setVisibility(View.GONE);
            quit.startAnimation(AnimationUtils.makeOutAnimation(this,true));
        }
    }

    void showPlot(){
        narrator.setText(currentNode.getPlot());
        narrator.bringToFront();
        narrator.startAnimation(AnimationUtils.makeInAnimation(this,false));
        narrator.setVisibility(View.VISIBLE);
    }

    void hidePlot(){
        if(narrator.getVisibility()==View.VISIBLE){
            narrator.startAnimation(AnimationUtils.makeOutAnimation(this,true));
            narrator.setVisibility(View.GONE);
        }
    }
}