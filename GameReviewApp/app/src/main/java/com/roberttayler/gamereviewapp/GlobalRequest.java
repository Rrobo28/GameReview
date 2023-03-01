package com.roberttayler.gamereviewapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class GlobalRequest {
    private static RequestQueue queue; // volley request queue

    // creates request queue based on provided context
    public static void create(Context context) {
        if (queue != null) {
            return;
        }
        queue = Volley.newRequestQueue(context.getApplicationContext());
    }

    // adds stringrequest to queue
    public static void add(StringRequest request) {
        if (queue == null) {
            Log.d("Queue", "Failed to find queue.");
            return;
        }
        queue.add(request);
    }
}
