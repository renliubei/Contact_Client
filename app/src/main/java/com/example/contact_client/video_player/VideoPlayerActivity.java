package com.example.contact_client.video_player;

import android.graphics.Typeface;
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
import com.example.contact_client.interactive_creator.VideoNode;
import com.example.contact_client.repository.VideoProject;
import com.example.contact_client.repository.mRepository;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class VideoPlayerActivity extends AppCompatActivity {
    public Typeface typeface;
    private static final int ROOT_NODE = -1;
    private VideoNode currentNode;
    private VideoProject mVideoProject;
    private MediaItem mediaItem;
    private  SimpleExoPlayer player;
    private mRepository mRepository;
    private PlayerView playerView;
    private View.OnClickListener onClickBtn;
    ConstraintSet constraintSet = new ConstraintSet();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    Button leftUpBtn,leftDownBtn,rightUpBtn,rightDownBtn;
    TextView narrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        mRepository = new mRepository(this);
        playerView = findViewById(R.id.videoPlayerView);
        getProject();
        initPlayer();
        narrator = findViewById(R.id.playerPlotNarrator);
        bindBtn();
        playProject();
    }

    void playProject(){
        currentNode = mVideoProject.getVideoNodeList().get(0);
        playCurrentNode();
    }

    void playCurrentNode(){
        if(currentNode.getId()==ROOT_NODE){
            playerView.hideController();
            showBtn(currentNode.getSons().size());
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
                        showBtn(currentNode.getSons().size());
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

    private void relocateBtn(int btnNum){
        constraintSet.clone(this,R.layout.activity_video_player);
        switch (btnNum){
            case 0:
                Log.d("mylo","on relocate buttons: no sons");
                break;
            case 1:
                constraintSet.clear(R.id.leftUpBtn);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.TOP,R.id.guideline_player_60h,ConstraintSet.TOP);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM);
                break;
            case 2:
                constraintSet.clear(R.id.leftUpBtn);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.TOP,R.id.guideline_player_60h,ConstraintSet.TOP);
                constraintSet.connect(R.id.leftUpBtn,ConstraintSet.BOTTOM,R.id.guideline_player_80h,ConstraintSet.BOTTOM);

                constraintSet.clear(R.id.rightUpBtn);
                constraintSet.connect(R.id.rightUpBtn,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START);
                constraintSet.connect(R.id.rightUpBtn,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END);
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
        constraintSet.constrainHeight(R.id.rightUpBtn,ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainWidth(R.id.leftUpBtn,ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(R.id.leftUpBtn,ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainWidth(R.id.rightDownBtn,ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(R.id.rightDownBtn,ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainWidth(R.id.leftDownBtn,ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(R.id.leftDownBtn,ConstraintSet.MATCH_CONSTRAINT);

        ConstraintLayout constraintLayout = getWindow().getDecorView().findViewById(R.id.playerLayout);
        constraintSet.applyTo(constraintLayout);
    }

    private void bindBtn(){
        leftDownBtn  = findViewById(R.id.leftDownBtn);
        rightDownBtn  = findViewById(R.id.rightDownBtn);
        leftUpBtn  = findViewById(R.id.leftUpBtn);
        rightUpBtn  = findViewById(R.id.rightUpBtn);

        onClickBtn = v -> {
                if(v==leftUpBtn){
                    currentNode = mVideoProject.getVideoNodeList().get(currentNode.getSons().get(0));
                }else if(v==rightUpBtn){
                    currentNode = mVideoProject.getVideoNodeList().get(currentNode.getSons().get(1));
                }else if(v==leftDownBtn){
                    currentNode = mVideoProject.getVideoNodeList().get(currentNode.getSons().get(2));
                }else if(v==rightDownBtn){
                    currentNode = mVideoProject.getVideoNodeList().get(currentNode.getSons().get(3));
                }
                playCurrentNode();
        };

        leftUpBtn.setOnClickListener(onClickBtn);
        rightDownBtn.setOnClickListener(onClickBtn);
        rightUpBtn.setOnClickListener(onClickBtn);
        leftDownBtn.setOnClickListener(onClickBtn);
    }

    private void setBtnVisible(int showNum){
        List<VideoNode> nodes = mVideoProject.getVideoNodeList();
        List<Integer> sons = currentNode.getSons();
        if(showNum>=1){
            leftUpBtn.setText(nodes.get(sons.get(0)).getBtnText());
            leftUpBtn.setAnimation(AnimationUtils.makeInAnimation(this,true));
            leftUpBtn.setVisibility(View.VISIBLE);
        }
        if(showNum>=2){
            rightUpBtn.setText(nodes.get(sons.get(1)).getBtnText());
            rightUpBtn.setAnimation(AnimationUtils.makeInAnimation(this,true));
            rightUpBtn.setVisibility(View.VISIBLE);
        }
        if(showNum>=3){
            leftDownBtn.setText(nodes.get(sons.get(2)).getBtnText());
            leftDownBtn.setAnimation(AnimationUtils.makeInAnimation(this,true));
            leftDownBtn.setVisibility(View.VISIBLE);
        }
        if(showNum>=4){
            rightDownBtn.setText(nodes.get(sons.get(3)).getBtnText());
            rightDownBtn.setAnimation(AnimationUtils.makeInAnimation(this,true));
            rightDownBtn.setVisibility(View.VISIBLE);
        }
    }

    private void showBtn(int bntNum){
        playerView.setClickable(false);
        relocateBtn(bntNum);
        setBtnVisible(bntNum);
    }

    private void hideAllBtn(){
        if(leftUpBtn.getVisibility()==View.VISIBLE){
            leftUpBtn.setAnimation(AnimationUtils.makeOutAnimation(this,true));
            leftUpBtn.setVisibility(View.GONE);
        }
        if(leftDownBtn.getVisibility()==View.VISIBLE){
            leftDownBtn.setAnimation(AnimationUtils.makeOutAnimation(this,true));
            leftDownBtn.setVisibility(View.GONE);
        }
        if(rightDownBtn.getVisibility()==View.VISIBLE){
            rightDownBtn.setAnimation(AnimationUtils.makeOutAnimation(this,true));
            rightDownBtn.setVisibility(View.GONE);
        }
        if(rightUpBtn.getVisibility()==View.VISIBLE){
            rightUpBtn.setAnimation(AnimationUtils.makeOutAnimation(this,true));
            rightUpBtn.setVisibility(View.GONE);
        }
    }

    void showPlot(){
        narrator.setText(currentNode.getPlot());
        narrator.setAnimation(AnimationUtils.makeInAnimation(this,false));
        narrator.setVisibility(View.VISIBLE);
        narrator.bringToFront();
    }

    void hidePlot(){
        if(narrator.getVisibility()==View.VISIBLE){
            narrator.setAnimation(AnimationUtils.makeOutAnimation(this,true));
            narrator.setVisibility(View.GONE);
        }
    }
}