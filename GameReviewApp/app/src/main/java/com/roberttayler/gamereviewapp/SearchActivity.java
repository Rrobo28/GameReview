package com.roberttayler.gamereviewapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private ArrayList<Game> newGames = new ArrayList<Game>();
    private RecyclerView newView;
    private SearchAdapter Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        newView = findViewById(R.id.searchGamesView);
        setTitle("Search Page");
    }

    // Sends a html request with the search information
    public void SearchPressed(View view) {
        newGames.clear();
        Log.d("SEARCH","RUNNING");
        EditText searchText = findViewById(R.id.editText);
        SetupRecyclerView(newView,Adapter,newGames,"fields *,age_ratings.rating,cover.image_id,genres.name,involved_companies.*,involved_companies.company.*,platforms.name,release_dates.*,screenshots.image_id;" +
                "search \"" + searchText.getText() + "\";" +
                "where cover.image_id != null;",null);
    }

    // This information is then converted into a Recycler View
    void SetupRecyclerView(RecyclerView view, SearchAdapter adapter, ArrayList<Game> games , String body, String url){
        HTMLRequest.GetGames(body, url, view, games);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        adapter = new SearchAdapter(this, games);
        view.setAdapter(adapter);
        view.setLayoutManager(layoutManager);
    }
}
