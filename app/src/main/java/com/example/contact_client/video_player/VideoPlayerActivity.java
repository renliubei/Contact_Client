package com.example.contact_client.video_player;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
    private static final int ROOT_NODE = -1;
    private VideoNode currentNode;
    private VideoProject mVideoProject;
    private MediaItem mediaItem;
    private  SimpleExoPlayer player;
    private mRepository mRepository;
    private ConstraintLayout constraintLayout;
    private PlayerView playerView;
    private View.OnClickListener onClickBtn;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    Button leftUpBtn,leftDownBtn,rightUpBtn,rightDownBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        mRepository = new mRepository(this);
        constraintLayout = getWindow().getDecorView().findViewById(R.id.playerLayout);
        playerView = findViewById(R.id.videoPlayerView);
        getProject();
        initPlayer();
        bindBtn();
        playProject();
    }

    void playProject(){
        currentNode = mVideoProject.getVideoNodeList().get(0);
        playCurrentNode();
    }

    void playCurrentNode(){
        if(currentNode.getId()==ROOT_NODE){
            showBtn(currentNode.getSons().size());
        }else{
            playerView.setClickable(false);
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

        ConstraintLayout.LayoutParams leftUpLayoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ConstraintLayout.LayoutParams leftDownLayoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ConstraintLayout.LayoutParams rightUpLayoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ConstraintLayout.LayoutParams rightDownLayoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ViewGroup.LayoutParams.WRAP_CONTENT);

        switch (btnNum){
            case 0:
                Log.d("mylo","on relocate buttons: no sons");
                break;
            case 1:
                leftUpLayoutParams.startToStart = constraintLayout.getId();
                leftUpLayoutParams.endToEnd = constraintLayout.getId();
                leftUpLayoutParams.topToTop = R.id.guideline_player_60h;
                leftUpLayoutParams.bottomToBottom = constraintLayout.getId();
                break;
            case 2:
                leftUpLayoutParams.startToStart = constraintLayout.getId();
                leftUpLayoutParams.endToEnd = constraintLayout.getId();
                leftUpLayoutParams.topToTop = R.id.guideline_player_60h;
                leftUpLayoutParams.bottomToBottom = R.id.guideline_player_80h;

                rightUpLayoutParams.startToStart = constraintLayout.getId();
                rightUpLayoutParams.endToEnd = constraintLayout.getId();
                rightUpLayoutParams.topToTop = R.id.guideline_player_80h;
                rightUpLayoutParams.bottomToBottom = constraintLayout.getId();
                break;
            case 3:
                leftUpLayoutParams.startToStart = constraintLayout.getId();
                leftUpLayoutParams.endToEnd = constraintLayout.getId();
                leftUpLayoutParams.topToTop = R.id.guideline_player_60h;
                leftUpLayoutParams.bottomToBottom = R.id.guideline_player_80h;

                rightUpLayoutParams.startToStart = constraintLayout.getId();
                rightUpLayoutParams.endToEnd = R.id.guideline_player_50v;
                rightUpLayoutParams.topToTop = R.id.guideline_player_80h;
                rightUpLayoutParams.bottomToBottom = constraintLayout.getId();

                leftDownLayoutParams.startToStart = R.id.guideline_player_50v;
                leftDownLayoutParams.endToEnd = constraintLayout.getId();
                leftDownLayoutParams.topToTop = R.id.guideline_player_80h;
                leftDownLayoutParams.bottomToBottom = constraintLayout.getId();
                break;
            default:
                leftUpLayoutParams.startToStart = constraintLayout.getId();
                leftUpLayoutParams.endToEnd = R.id.guideline_player_50v;
                leftUpLayoutParams.topToTop = R.id.guideline_player_60h;
                leftUpLayoutParams.bottomToBottom = R.id.guideline_player_80h;

                rightUpLayoutParams.startToStart = R.id.guideline_player_50v;
                rightUpLayoutParams.endToEnd = constraintLayout.getId();
                rightUpLayoutParams.topToTop = R.id.guideline_player_60h;
                rightUpLayoutParams.bottomToBottom = R.id.guideline_player_80h;

                leftDownLayoutParams.startToStart = constraintLayout.getId();
                leftDownLayoutParams.endToEnd = R.id.guideline_player_50v;
                leftDownLayoutParams.topToTop = R.id.guideline_player_80h;
                leftDownLayoutParams.bottomToBottom = constraintLayout.getId();

                rightDownLayoutParams.startToStart = R.id.guideline_player_50v;
                rightDownLayoutParams.endToEnd = constraintLayout.getId();
                rightDownLayoutParams.topToTop = R.id.guideline_player_80h;
                rightDownLayoutParams.bottomToBottom = R.id.guideline_player_50v;
                break;
        }
        leftUpBtn.setLayoutParams(leftUpLayoutParams);
        rightUpBtn.setLayoutParams(rightUpLayoutParams);
        leftDownBtn.setLayoutParams(leftDownLayoutParams);
        rightDownBtn.setLayoutParams(rightDownLayoutParams);
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

        leftDownBtn.setOnClickListener(onClickBtn);
        rightDownBtn.setOnClickListener(onClickBtn);
        leftUpBtn.setOnClickListener(onClickBtn);
        leftDownBtn.setOnClickListener(onClickBtn);
    }

    private void setBtnVisible(int showNum){
        List<VideoNode> nodes = mVideoProject.getVideoNodeList();
        List<Integer> sons = currentNode.getSons();
        if(showNum>=1){
            leftDownBtn.setText(nodes.get(sons.get(0)).getName());
            leftUpBtn.setVisibility(View.VISIBLE);

        }
        if(showNum>=2){
            rightUpBtn.setText(nodes.get(sons.get(1)).getName());
            rightUpBtn.setVisibility(View.VISIBLE);
        }
        if(showNum>=3){
            leftDownBtn.setText(nodes.get(sons.get(3)).getName());
            leftDownBtn.setVisibility(View.VISIBLE);
        }
        if(showNum>=4){
            rightDownBtn.setText(nodes.get(sons.get(4)).getName());
            rightDownBtn.setVisibility(View.VISIBLE);
        }
    }

    private void showBtn(int bntNum){
        relocateBtn(bntNum);
        setBtnVisible(bntNum);
    }

    private void hideAllBtn(){
        if(leftUpBtn.getVisibility()==View.VISIBLE){
            leftUpBtn.setVisibility(View.GONE);
        }
        if(leftDownBtn.getVisibility()==View.VISIBLE){
            leftDownBtn.setVisibility(View.GONE);
        }
        if(rightDownBtn.getVisibility()==View.VISIBLE){
            rightDownBtn.setVisibility(View.GONE);
        }
        if(rightUpBtn.getVisibility()==View.VISIBLE){
            rightDownBtn.setVisibility(View.GONE);
        }
    }
}