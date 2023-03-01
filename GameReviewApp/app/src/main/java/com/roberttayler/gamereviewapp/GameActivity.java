package com.roberttayler.gamereviewapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.roberttayler.gamereviewapp.comments.Comment;
import com.roberttayler.gamereviewapp.comments.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    private Game thisGame;

    RecyclerView commentView;
    CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        GetGame();
        GetComments();
    }

    void GetGame(){
        thisGame = (Game) getIntent().getSerializableExtra("Game");
        setTitle(thisGame.getName());

        // finds all text and image views
        TextView title = findViewById(R.id.game_name);
        TextView devs = findViewById(R.id.game_devs);
        TextView publisher = findViewById(R.id.game_pub);
        TextView genre = findViewById(R.id.game_genres);
        TextView platforms = findViewById(R.id.game_platforms);
        TextView reviewCount = findViewById(R.id.review_count);
        TextView description = findViewById(R.id.game_desc);
        TextView releaseDate = findViewById(R.id.game_release_date);
        TextView rating = findViewById(R.id.game_rating);
        ImageView image = findViewById(R.id.game_image);
        TextView ageRating = findViewById(R.id.age_rating);

        // Picasso takes an image from a webpage and passes it into a image view
        Picasso.with(this).load("https://images.igdb.com/igdb/image/upload/t_cover_big/"+thisGame.getImageURL()+".jpg").into(image);

        //Takes the information from the game class and displays it
        title.setText(thisGame.getName());
        reviewCount.setText("("+thisGame.getReviewCount()+") Reviewers");
        releaseDate.setText("Release Date: "+thisGame.getReleaseDate());
        description.setText(Html.fromHtml("Summary: <font color=black>"+thisGame.getDescription() + "</font>"));
        rating.setText(thisGame.getRating()+"%");
        platforms.setText(Html.fromHtml(getList(thisGame.getPlatforms(),"Platforms")));
        ageRating.setText(Html.fromHtml("Age Rating: <font color=black>" + thisGame.getAgeRating() + "</font>"));
        genre.setText(Html.fromHtml(getList(thisGame.getGenres(),"Genres")));
        devs.setText(Html.fromHtml("Developer: <font color=black>"+thisGame.getDeveloper()+"</font>"));
        publisher.setText(Html.fromHtml("Publisher: <font color=black>"+thisGame.getPublisher()+"</font>"));

        //Creates new images views for all of the screen shots
        for(int i = 0; i < thisGame.getScreenshots().size();i++){
            ImageView imageView = new ImageView(image.getContext());
            imageView.setPadding(0,70,50,0);
            Picasso.with(this).load("https://images.igdb.com/igdb/image/upload/t_screenshot_big/"+thisGame.getScreenshotsbyIndex(i)+".jpg").into(imageView);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.imageLayout);
            linearLayout.addView(imageView);
        }
    }

    //Sets up the RecyclerView for the comments
    void GetComments(){
        commentView = findViewById(R.id.comment_View);
        ArrayList<Comment> commentList = new ArrayList<Comment>();
        SetupRecyclerView(commentView, thisGame.getId());
    }

    void SetupRecyclerView(RecyclerView view, int gameid){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        CommentAdapter adapter = new CommentAdapter(this, gameid);
        view.setAdapter(adapter);
        view.setLayoutManager(layoutManager);
        this.adapter = adapter;
    }

    // Add comment takes you to a new activity
    public void AddComment(View view) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null && MainActivity.signedIn) {
            Intent intent = new Intent(this, AddComment.class);
            this.startActivityForResult(intent, 0);
        }
        else {
            Toast toast = Toast.makeText(this,"Not signed in. Please sign in at the top", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //When the comment activity is finished it will return with an intent that has the content and rating of the comment
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if(requestCode == 0 && data != null && data.hasExtra("comment") && data.hasExtra("rating")) {
            //Create Comment
           String content = data.getStringExtra("comment");
           String rating = data.getStringExtra("rating");
           Log.d("COMMENT",content);
           FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

           if (user == null) {
               return;
           }

           int i_rating = 0;

           try {
               i_rating = Integer.parseInt(rating);
               if (i_rating < 0 || i_rating > 10) {
                   return;
               }
           } catch (NumberFormatException e) {
               return;
           }

           Comment newComment = new Comment(thisGame.getId(), i_rating, new User(user.getUid(), user.getDisplayName()), content, Timestamp.now());
           //Comment is uploaded to the FireStore
           newComment.upload();
           adapter.addComment(newComment);
       }
    }

    //Get list will return a formatted sting for the game information lists
    String getList(ArrayList<String> list, String name){
        String stringList = name+": ";
        for(int i = 0; i < list.size();i++) {
            if (i == list.size() - 1) {
                Log.d("PLATFORM", list.get(i));
                stringList += "<font color=black>"+list.get(i) + "</font>";
            }
            else{
                stringList +=  "<font color=black>"+list.get(i) + ", </font>";
            }
        }
        return stringList;
    }
}
