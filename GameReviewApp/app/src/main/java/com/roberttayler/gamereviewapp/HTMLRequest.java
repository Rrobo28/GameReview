package com.roberttayler.gamereviewapp;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HTMLRequest {
    public static void GetGames(String body, String URL, RecyclerView newView, ArrayList<Game> games){
        String gameURL = "https://api.igdb.com/v4/games/";
        VolleyPost gamePost = new VolleyPost(gameURL).body(body);
        gamePost.asJSONArray(gameArray -> {
            for (int i = 0; i < gameArray.length(); i++) {
                Game game = new Game(gameArray, i);
                games.add(game);
                newView.getAdapter().notifyItemInserted(games.size());
            }
        });
    }
}
