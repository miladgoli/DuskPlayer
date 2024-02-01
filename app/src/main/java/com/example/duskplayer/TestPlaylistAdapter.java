package com.example.duskplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TestPlaylistAdapter extends RecyclerView.Adapter<TestPlaylistAdapter.TestViewHolder> {



    private String[] name=new String[6];

    public TestPlaylistAdapter(){
        name[0]="Playlist0";
        name[1]="Playlist2";
        name[2]="Playlist3";
        name[3]="Playlist4";
        name[4]="Playlist5";
        name[5]="Playlist6";

    }
    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist,parent,false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        holder.bindTest(name[position]);
    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    public class TestViewHolder extends RecyclerView.ViewHolder{
        private ImageView igPlaylist;
        private TextView namePlaylist;
        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            igPlaylist =itemView.findViewById(R.id.igPlaylistItem);
            namePlaylist =itemView.findViewById(R.id.namePlaylistItemTxt);


        }
        public void bindTest(String playlist){
            namePlaylist.setText(playlist.substring(0,8));
        }
    }
}
