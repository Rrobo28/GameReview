package com.roberttayler.gamereviewapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.GameViewHolder> {
    private final ArrayList<Game> gameList;
    private LayoutInflater inflater;
    private Context context;
    //Does the same thing as the CommentAdapter class just with a different layout and the games class rather then the comment class
    public GamesAdapter(Context context, ArrayList<Game> popularGameList){
        inflater = LayoutInflater.from(context);
        this.gameList = popularGameList;
        this.context = context;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.game_item,parent,false);
        return new GameViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull GamesAdapter.GameViewHolder holder, int position) {
        Game game = gameList.get(position);
        holder.gameName.setText(game.getName());
        holder.gameRating.setText(game.getRating()+"%");
        Picasso.with(holder.adapter.context).load("https://images.igdb.com/igdb/image/upload/t_cover_big/"+game.getImageURL()+".jpg").into(holder.frontCover);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,GameActivity.class);
                intent.putExtra("Game" ,game);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {
        public final TextView gameName;
        public final TextView gameRating;
        public final ImageView frontCover;
        final GamesAdapter adapter;
        public ConstraintLayout constraintLayout;
        public GameViewHolder(@NonNull View itemView, GamesAdapter adapter) {
            super(itemView);
            gameName = itemView.findViewById(R.id.game_name);
            gameRating = itemView.findViewById(R.id.game_rating);
            frontCover = itemView.findViewById(R.id.game_cover);
            this.adapter = adapter;
            this.constraintLayout = itemView.findViewById(R.id.const_layout);
        }
    }
}
