package raja.com.izunaplayer.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import raja.com.izunaplayer.R;
import raja.com.izunaplayer.client.MediaBrowserHelper;
import raja.com.izunaplayer.service.MusicService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int READ_EXTERNAL_STORAGE_OK = 1;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private List<Song> songsList = new ArrayList<>();
    private SongsListAdapter mAdapter;
    private MediaBrowserHelper mMediaBrowserHelper;
    private MediaControllerCompat mController;
    private boolean mIsPlaying;
    public static final String EXTRA_CURRENT_MEDIA_DESCRIPTION =
            "raja.com.izunaplayer.CURRENT_MEDIA_DESCRIPTION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissionAndLoad();


    }

    private void checkPermissionAndLoad(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE_OK);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            initView();
        }
    }


    private void initView(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMediaBrowserHelper = new MediaBrowserConnection(this);
        mMediaBrowserHelper.registerCallback(new MediaBrowserListener());
        new loadSongs().execute("");
    }



    @Override
    protected void onStart() {
        super.onStart();
        mMediaBrowserHelper.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Song song = songsList.get(pos);
        String mediaId = song.id+"";
        Bundle bundle = new Bundle();
        bundle.putInt("POS",pos);
        mController.getTransportControls().playFromMediaId(mediaId,bundle);
        Intent intent = new Intent(MainActivity.this,NowPlayingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        MediaMetadataCompat metadata = mController.getMetadata();
        System.out.println("Test");
        if(metadata != null){
            intent.putExtra(MainActivity.EXTRA_CURRENT_MEDIA_DESCRIPTION,
                    metadata.getDescription());
        }
        startActivity(intent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    initView();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private  class loadSongs extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            songsList = SongsLoader.getAllSongs(MainActivity.this);
            mAdapter = new SongsListAdapter(MainActivity.this,songsList);
            return "Done";
        }

        @Override
        protected void onPostExecute(String s) {
            recyclerView.setAdapter(mAdapter);
            //recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL_LIST));
        }
    }


    private class MediaBrowserConnection extends MediaBrowserHelper{

        private MediaBrowserConnection(Context context) {
            super(context, MusicService.class);
        }

        @Override
        protected void onConnected(@NonNull MediaControllerCompat mediaController) {
            mController = mediaController;
        }

        @Override
        protected void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
            super.onChildrenLoaded(parentId, children);

            final MediaControllerCompat mediaController = getMediaController();

            // Queue up all media items for this simple sample.
            for (final MediaBrowserCompat.MediaItem mediaItem : children) {
                mediaController.addQueueItem(mediaItem.getDescription());
            }

            // Call prepare now so pressing play just works.
            //mediaController.getTransportControls().prepare();

        }
    }


    private class MediaBrowserListener extends MediaControllerCompat.Callback {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat playbackState) {
            mIsPlaying = playbackState != null &&
                    playbackState.getState() == PlaybackStateCompat.STATE_PLAYING;
            //mMediaControlsImage.setPressed(mIsPlaying);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat mediaMetadata) {
            if (mediaMetadata == null) {
                return;
            }
//            mTitleTextView.setText(mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
//            mArtistTextView.setText(mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
//            mAlbumArt.setImageBitmap(MusicLibrary.getAlbumBitmap(
//                    MainActivity.this,
//                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)));
        }

        @Override
        public void onSessionDestroyed() {
            super.onSessionDestroyed();
        }

        @Override
        public void onQueueChanged(List<MediaSessionCompat.QueueItem> queue) {
            super.onQueueChanged(queue);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        mMediaBrowserHelper.onStop();
    }
}
