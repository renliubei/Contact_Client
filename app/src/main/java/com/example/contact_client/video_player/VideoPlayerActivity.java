package com.example.contact_client.video_player;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.contact_client.R;
import com.example.contact_client.repository.VideoCut;
import com.example.contact_client.repository.VideoProject;
import com.example.contact_client.repository.mRepository;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class VideoPlayerActivity extends AppCompatActivity {
    private VideoCut mVideoCut;
    private VideoProject mVideoProject;
    private  mRepository mRepository;
    private MediaItem mediaItem;
    private  SimpleExoPlayer player;
    private PlayerView playerView;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        mRepository = new mRepository(this);
        initPlayer();
        getProject();
        if(mVideoProject!=null) getVideoAndPlay(mVideoProject.getVideoNodeList().get(1).getId());
    }

    void getProject(){
        mVideoProject = getIntent().getParcelableExtra(getString(R.string.videoProject));
        if(mVideoProject==null){
            Toasty.error(getApplicationContext(),"未读取到互动视频",Toasty.LENGTH_SHORT,true).show();
            finish();
        }
    }

    void getVideoAndPlay(long cutID){
        mDisposable.add(
                mRepository.findVideoCutById(cutID)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(videoCut -> {
                                mediaItem = MediaItem.fromUri("/storage/emulated/0/Movies/Aegean_Sea.mp4");
                                player.addMediaItem(mediaItem);
                                player.prepare();
                                player.play();
                        }, throwable -> {throwable.printStackTrace();Toasty.error(getApplicationContext(), "加载错误",Toasty.LENGTH_SHORT,true).show();finish();})
        );
    }

    void initPlayer(){
        player = new SimpleExoPlayer.Builder(this).build();

        playerView = findViewById(R.id.videoPlayerView);
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
                        break;
                    default:
                        stateString = "UNKNOWN_STATE             -";
                        break;
                }
                Log.d("mylo", "changed state to " + stateString);
            }
        });
        player.setPlayWhenReady(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}