package raja.com.izunaplayer.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import raja.com.izunaplayer.R;

public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.MyViewHolder> {

    private MainActivity context;
    private List<Song> list;

    public SongsListAdapter(MainActivity context, List<Song> list){
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song,parent,false);
        rootView.setOnClickListener(context);
        MyViewHolder viewHolder = new MyViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder itemHolder, int position) {
        Song song = list.get(position);
        itemHolder.title.setText(song.title);
        itemHolder.artist.setText(song.artistName);
    }

    @Override
    public int getItemCount() {
        return list != null? list.size() : 0;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        protected TextView title, artist;
        protected ImageView albumArt;


        public MyViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.song_title);
            this.artist = (TextView) view.findViewById(R.id.song_artist);
            this.albumArt = (ImageView) view.findViewById(R.id.albumArt);

        }



    }
}
