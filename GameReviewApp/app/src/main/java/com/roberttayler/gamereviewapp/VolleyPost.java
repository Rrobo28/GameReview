package com.roberttayler.gamereviewapp;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyPost {
    private static final String CLIENT_ID = "8k3l4l9cfrs0s3kufb4hr0k0yobyyh";
    private static final String AUTH = "Bearer leu99aberhjvf76ekad3nj4og71dxk";

    private String base_url; // volley url
    private Map<String, String> header; // header elements
    private String body; // body of post

    public VolleyPost(String base_url) {
        this.base_url = base_url;
        this.header = new HashMap<String, String>();
        this.body = "";
        header("Client-ID", CLIENT_ID); // insert clientid header
        header("Authorization", AUTH); // insert auth header
    }

    // inserts a header value
    public VolleyPost header(String key, String value) {
        header.put(key, value);
        return this;
    }

    // appends to the body
    public VolleyPost body(String body) {
        this.body += body;
        return this;
    }

    // execute action with query result as provided json array
    public StringRequest asJSONArray(action<JSONArray> action) {
        return request(from -> {
            JSONArray result;
            try {
                result = new JSONArray(from);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
            action.execute(result);
        });
    }

    // execute action with query result as provided json object
    public StringRequest asJSONObject(action<JSONObject> action) {
        return request(from -> {
            JSONObject result = null;
            JSONArray arr;
            try {
                result = new JSONObject(from);
            } catch (JSONException e) {

            }
            if (result == null) {
                try {
                    arr = new JSONArray(from);
                    result = arr.getJSONObject(0);
                } catch (JSONException e) {

                }
            }
            if (result == null) {
                return;
            }
            action.execute(result);
        });
    }

    // execute action with query result as provided string
    public StringRequest asString(action<String> action) {
        return request(from -> action.execute(from));
    }

    // creates a string request based on this class instance's values, sends to requestQueue
    private StringRequest request(action<String> action) {
        StringRequest request = new StringRequest(Request.Method.POST, base_url, action::execute, error -> Log.d("ERROR","error => " + error.toString())) {
            @Override
            public byte[] getBody() {
                return body.getBytes();
            }
            @Override
            public Map<String, String> getHeaders() {
                return header;
            }
        };
        GlobalRequest.add(request);
        return request;
    }

    // interface used for actions, accepts a generic type
    @FunctionalInterface
    public interface action<T> {
        void execute(T t);
    }
}
