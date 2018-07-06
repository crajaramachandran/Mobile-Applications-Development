package raja.com.izunaplayer.ui;

import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import raja.com.izunaplayer.R;
import raja.com.izunaplayer.service.MusicService;

public class NowPlayingActivity extends AppCompatActivity implements ValueAnimator.AnimatorUpdateListener {

    private TextView mTitleTextView;
    private TextView mArtistTextView;
    private ImageView mMediaControlsImage;
    private MediaBrowserCompat mMediaBrowser;
    private MediaControllerCompat mController;
    private PlaybackStateCompat mLastPlaybackState;
    private SeekBar mSeekbar;
    private ValueAnimator mProgressAnimator;
    private boolean updateSeekbar=false;
    private boolean mIsTracking;


    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mIsTracking = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mController.getTransportControls().seekTo(mSeekbar.getProgress());
            mIsTracking = false;
        }
    };


    private final MediaControllerCompat.Callback mMediaCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            updatePlaybackState(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            if(metadata != null){
                updateMediaDescription(metadata.getDescription());
            }
        }
    };


    private final MediaBrowserCompat.ConnectionCallback mConnectionCallback = new MediaBrowserCompat.ConnectionCallback(){

        @Override
        public void onConnected() {
            try {
                connectToSession(mMediaBrowser.getSessionToken());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowplaying);
        mTitleTextView = findViewById(R.id.song_title);
        mArtistTextView = findViewById(R.id.song_artist);
        mMediaControlsImage = findViewById(R.id.media_controls);
        mSeekbar = findViewById(R.id.seekbar_audio);
        mSeekbar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);

        final ClickListener clickListener = new ClickListener();
        findViewById(R.id.button_previous).setOnClickListener(clickListener);
        findViewById(R.id.button_play).setOnClickListener(clickListener);
        findViewById(R.id.button_next).setOnClickListener(clickListener);

        mMediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, MusicService.class), mConnectionCallback, null);
        updateFromparams(getIntent());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMediaBrowser != null) {
            mMediaBrowser.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMediaBrowser != null) {
            mMediaBrowser.disconnect();
        }
        MediaControllerCompat controllerCompat = MediaControllerCompat.getMediaController(NowPlayingActivity.this);
        if (controllerCompat != null) {
            controllerCompat.unregisterCallback(mMediaCallback);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void connectToSession(MediaSessionCompat.Token token) throws RemoteException {
        MediaControllerCompat mediaController= new MediaControllerCompat(NowPlayingActivity.this,token);
        if(mediaController.getMetadata() == null){
            finish();
            return;
        }
        mController= mediaController;
        MediaControllerCompat.setMediaController(NowPlayingActivity.this,mediaController);
        mediaController.registerCallback(mMediaCallback);
        PlaybackStateCompat state = mediaController.getPlaybackState();
        updatePlaybackState(state);
        MediaMetadataCompat metadata = mediaController.getMetadata();
        if(metadata != null){
            updateMediaDescription(metadata.getDescription());
            //updateDuration(metadata);
        }
    }

    private void updateFromparams(Intent intent){
        if (intent != null) {
            MediaDescriptionCompat description = intent.getParcelableExtra(
                    MainActivity.EXTRA_CURRENT_MEDIA_DESCRIPTION);
            if (description != null) {
                updateMediaDescription(description);
                seekBarInit();
            }
        }
    }

    private void seekBarInit(){
        MediaMetadataCompat metadata = mController.getMetadata();
        final int max = metadata != null
                ? (int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
                : 0;
        mSeekbar.setProgress(0);
        mSeekbar.setMax(max);
    }

    private void updateMediaDescription(MediaDescriptionCompat description){
        if(description == null){
            return;
        }
        mTitleTextView.setText(description.getTitle());
        mArtistTextView.setText(description.getSubtitle());
    }

    private void updatePlaybackState(PlaybackStateCompat state){
        if(state == null){
            return;
        }
        mLastPlaybackState = state;

        switch(state.getState()){
            case PlaybackStateCompat.STATE_PLAYING:
                mMediaControlsImage.setPressed(true);
                seekBarUpdate(state);
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                mMediaControlsImage.setPressed(false);
                break;
        }

    }


    private void seekBarUpdate(PlaybackStateCompat state){
        if(!updateSeekbar){
            seekBarInit();
            updateSeekbar = true;
        }
        // If there's an ongoing animation, stop it now.
        if (mProgressAnimator != null) {
            mProgressAnimator.cancel();
            mProgressAnimator = null;
        }

        final int progress = state != null
                ? (int) state.getPosition()
                : 0;
        mSeekbar.setProgress(progress);

        int gm = mSeekbar.getMax();
        mSeekbar.setProgress(gm);
        Toast.makeText(NowPlayingActivity.this,gm+"",Toast.LENGTH_SHORT).show();
        if (state != null && state.getState() == PlaybackStateCompat.STATE_PLAYING) {
            final int timeToEnd = (int) ((mSeekbar.getMax() - progress) / state.getPlaybackSpeed());
            mProgressAnimator = ValueAnimator.ofInt(progress, mSeekbar.getMax()).setDuration(timeToEnd);
            mProgressAnimator.setInterpolator(new LinearInterpolator());
            mProgressAnimator.addUpdateListener(this);
            mProgressAnimator.start();
        }

    }


    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        Log.d("ANIM_TAG",valueAnimator.getAnimatedValue()+"");
        if (mIsTracking) {
            valueAnimator.cancel();
            return;
        }
        final int animatedIntValue = (int) valueAnimator.getAnimatedValue();
        mSeekbar.setProgress(animatedIntValue);
    }


    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_previous:
                    mController.getTransportControls().skipToPrevious();
                    break;
                case R.id.button_play:
                    if (mController.getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING) {
                        mController.getTransportControls().pause();

                    } else {
                        mController.getTransportControls().play();
                    }
                    break;
                case R.id.button_next:
                    mController.getTransportControls().skipToNext();
                    break;
            }
        }
    }

}
