package com.roberttayler.gamereviewapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    public static boolean signedIn = false;

    private RecyclerView playstationView;
    private RecyclerView xboxView;
    private RecyclerView ps5View;
    private RecyclerView popularView;
    private GamesAdapter popularAdapter;

    //List of games to be shown
    private ArrayList<Game> newGames = new ArrayList<Game>();
    private ArrayList<Game> bestPS4 = new ArrayList<Game>();
    private ArrayList<Game> bestXbox = new ArrayList<Game>();
    private ArrayList<Game> bestPS5 = new ArrayList<Game>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalRequest.create(this);
        setContentView(R.layout.activity_main);
        //Recycler Views
        popularView = findViewById(R.id.new_view);
        playstationView =  findViewById(R.id.best_ps4);
        ps5View = findViewById(R.id.best_pc);
        xboxView = findViewById(R.id.best_xbox);

        //HTML query for most popular games per platform.
        SetupRecyclerView(popularView,popularAdapter,newGames,"fields *,age_ratings.rating,cover.image_id,genres.name,involved_companies.*,involved_companies.company.*,platforms.name,release_dates.*,screenshots.image_id;" +
                "where aggregated_rating != null & aggregated_rating > 80 & first_release_date != null;" +
                "sort first_release_date desc;",null);
        SetupRecyclerView(playstationView,popularAdapter,bestPS4,"fields *,age_ratings.rating,cover.image_id,genres.name,involved_companies.*,involved_companies.company.*,platforms.name,release_dates.*,screenshots.image_id;" +
                "where platforms = 48 & category != 1 & category != 2 & category != 5 & aggregated_rating != null & first_release_date != null ;" +
                "sort aggregated_rating desc;",null);
        SetupRecyclerView(xboxView,popularAdapter,bestXbox,"fields *,age_ratings.rating,cover.image_id,genres.name,involved_companies.*,involved_companies.company.*,platforms.name,release_dates.*,screenshots.image_id;" +
                "where platforms = 49  & category != 1 & category != 2 & category != 5 & aggregated_rating != null & first_release_date != null ;" +
                "sort aggregated_rating desc;",null);
        SetupRecyclerView(ps5View,popularAdapter,bestPS5,"fields *,age_ratings.rating,cover.image_id,genres.name,involved_companies.*,involved_companies.company.*,platforms.name,release_dates.*,screenshots.image_id;" +
                "where platforms = 167 & category != 1 & category != 2 & category != 5 & aggregated_rating != null & first_release_date != null;" +
                "sort aggregated_rating desc;",null);

        // Title of the page
        setTitle("Top Game Review");
    }

    //Function to setup the Recycler Views and binds them to the adapter
    //This function also calls a html request to the API
    void SetupRecyclerView(RecyclerView view,GamesAdapter adapter, ArrayList<Game> games , String body, String url){
        HTMLRequest.GetGames(body,url,view,games);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        adapter = new GamesAdapter(this, games);
        view.setAdapter(adapter);
        view.setLayoutManager(layoutManager);
    }

    //Creates the action bar which are the buttons in the header of the app
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle menu buttons
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.search){
            Intent intent = new Intent(this,SearchActivity.class);
            this.startActivity(intent);
        }
        //Sign in
        if (id == R.id.action_signin && !signedIn) {
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.GoogleBuilder().build());
            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
            signedIn = true;
        }
        //Sign Out
        if(id == R.id.action_signout){
            Toast toast = Toast.makeText(this,"Signed Out", Toast.LENGTH_SHORT);
            toast.show();
            AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        signedIn = false;
                    }
                });
        }
        return super.onOptionsItemSelected(item);
    }

    //Result of the sign in
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                Toast toast2 = Toast.makeText(this,"Signed In", Toast.LENGTH_SHORT);
                toast2.show();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                signedIn = true;
            } else {
                //response.getError().getErrorCode();
            }
        }
    }
}
