package com.roberttayler.gamereviewapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    private String
        name = "None",
        developer =  "None",
        publisher = "None",
        rating =  "None",
        description = "None",
        reviewCount = "None",
        releaseDate = "None",
        imageURL = "None",
        ageRating =  "None";

    private String[] ratings = {"0","3","7","12","16","18","RP (Rating Pending)","EC (Early Childhood)","E (Everyone)","E10+ (Everyone 10+)","T (Teen)","M (Mature)","AO (Adults Only)"};

    private ArrayList<String>
            genres = new ArrayList<String>(),
            platforms = new ArrayList<String>(),
            screenShots = new ArrayList<String>();

    private int id;

    private final transient JSONHelper helper;

    public Game(JSONArray arr, int index) {
        this(new JSONHelper(arr, index));
    }

    public Game(JSONObject object) {
        this(new JSONHelper(object));
    }

    public Game(JSONHelper helper) {
        this.helper = helper;
        init();
    }

    private void init() {
        //ID
        setId(Integer.parseInt(helper.tryGet(true,"id")));
        //Name
        setName(helper.tryGet("name"));
        //Rating
        setRating(helper.tryGet(true, "aggregated_rating"));
        //Summery
        setDescription(helper.tryGet("summary"));
        //Review Count
        setReviewCount(helper.tryGet(true, "aggregated_rating_count"));
        //Release Date
        JSONHelper dateHelper = helper.tryGetArray("release_dates",0);
        if(dateHelper !=null)
        setReleaseDate(dateHelper.tryGet("human"));
        Log.d("DATE", helper.tryGet("first_release_date"));
        //Cover Image
        setImageURL(helper.tryGet("cover.image_id"));
        //Age Rating
        JSONArray ageRatingArray = helper.tryGetArray("age_ratings");
        for(int i = 0; i < ageRatingArray.length();i++){
            JSONHelper ageHelper = new JSONHelper(ageRatingArray,i);
            setAgeRating(ratings[Integer.parseInt(ageHelper.tryGet(true,"rating"))]);
        }
        //Genres
        JSONArray genreArray = helper.tryGetArray("genres");
        for(int i = 0; i < genreArray.length();i++){
            JSONHelper genreHelper = new JSONHelper(genreArray,i);
            addGenres(genreHelper.tryGet("name"));
        }
        //Screenshots
        JSONArray screenShotsArray = helper.tryGetArray("screenshots");
        for(int i = 0; i < screenShotsArray.length();i++){
            JSONHelper screenShotHelper = new JSONHelper(screenShotsArray,i);
            addScreenShots(screenShotHelper.tryGet("image_id"));
        }
        //Platforms
        JSONArray platformsArray = helper.tryGetArray("platforms");
        for(int i = 0; i < platformsArray.length();i++){
            JSONHelper platformHelper = new JSONHelper(platformsArray,i);
            addPlatform(platformHelper.tryGet("name"));
        }
        //Developers
        JSONArray devArray = helper.tryGetArray("involved_companies");
        for(int i = 0; i < devArray.length();i++){
            JSONHelper devHelper = new JSONHelper(devArray,i);
            try {
                Log.d("COMPANY", String.valueOf(devArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(devHelper.tryGet("developer")=="true" && devHelper.tryGet("publisher")=="true" ) {
                setDeveloper(devHelper.tryGet("company.name"));
                setPublisher(devHelper.tryGet("company.name"));
            }
            else if(devHelper.tryGet("developer")=="true"){
                setDeveloper(devHelper.tryGet("company.name"));
            }
            else if(devHelper.tryGet("publisher")=="true") {
                setPublisher(devHelper.tryGet("company.name"));
            }
        }
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }

    public void addScreenShots(String screenShots) {
        this.screenShots.add(screenShots);
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void addGenres(String genre) {
        this.genres.add(genre);
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(String reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPlatforms() {
        return platforms;
    }

    public void addPlatform(String platform) {
        this.platforms.add(platform);
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getScreenshotsbyIndex(int index) {
        return screenShots.get(index);
    }

    public ArrayList<String> getScreenshots() {
        return screenShots;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
