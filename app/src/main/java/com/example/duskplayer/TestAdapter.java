package com.example.duskplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {



    private String[] name=new String[15];

    public TestAdapter(){
        name[0]="agajfejio";
        name[1]="agajfejio";
        name[2]="agajfejfdfio";
        name[3]="dfdsfdsfsfdsg";
        name[4]="agajfejio";
        name[5]="agaasdjafejio";
        name[6]="agajfejidsfsdfo";
        name[7]="agajfesdfjiosd";
        name[8]="agajfejiossdffsfd";
        name[9]="sdfsdfagajfejio";
        name[10]="adsfsdfgajfejio";
        name[11]="agthyhajfejio";
        name[12]="aregajfejio";
        name[13]="a345345gajfejio";
        name[14]="dfgdhthagajfejio";

    }
    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music,parent,false);
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
        private ImageView igMusic;
        private TextView nameMusic;
        private TextView singerMusic;
        private ImageButton moreMusic;
        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            igMusic=itemView.findViewById(R.id.coverItemMusic);
            nameMusic=itemView.findViewById(R.id.musicNameItemMusic);
            singerMusic=itemView.findViewById(R.id.singerNameItemMusic);
            moreMusic=itemView.findViewById(R.id.moreMusicItemMusic);


        }
        public void bindTest(String Music){
            nameMusic.setText(Music.substring(1,3));
            singerMusic.setText(Music.substring(4,6));
        }
    }
}
