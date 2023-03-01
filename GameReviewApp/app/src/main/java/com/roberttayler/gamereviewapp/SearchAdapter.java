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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.GameViewHolder> {
    private final ArrayList<Game> searchGameList;
    private LayoutInflater inflater;
    private Context context;

    // Does the same thing as the GameAdapter, however it uses a different layout.
    public SearchAdapter(Context context, ArrayList<Game> popularGameList){
        inflater = LayoutInflater.from(context);
        this.searchGameList = popularGameList;
        this.context = context;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.search_game_item,parent,false);
        return new GameViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.GameViewHolder holder, int position) {
        Game game = searchGameList.get(position);
        holder.gameName.setText(game.getName());
        String genrelist = null;
        if(game.getGenres().size() > 0)
        genrelist = game.getGenres().get(0);

        holder.gameGenre.setText(genrelist);
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
        return searchGameList.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {
        public final TextView gameName;
        public final TextView gameGenre;
        public final ImageView frontCover;
        final SearchAdapter adapter;
        public ConstraintLayout constraintLayout;

        public GameViewHolder(@NonNull View itemView, SearchAdapter adapter) {
            super(itemView);
            gameName = itemView.findViewById(R.id.game_name);
            frontCover = itemView.findViewById(R.id.gameCover);
            gameGenre = itemView.findViewById(R.id.gameGenre);
            this.adapter = adapter;
            this.constraintLayout = itemView.findViewById(R.id.constraint_layout_search);
        }
    }
}
