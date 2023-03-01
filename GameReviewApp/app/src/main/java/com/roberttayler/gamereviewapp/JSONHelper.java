package com.roberttayler.gamereviewapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// an object that stores a JSONObject and interacts in a way that prevents boiler-plate code
public class JSONHelper {
    private JSONObject object; // the JSONObject being worked on

    public JSONHelper(JSONObject object) {
        setObject(object);
    }

    // takes object from array to use
    public JSONHelper(JSONArray array, int index) {
        try {
            this.object = array.getJSONObject(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // sets the object being worked on based on array and index
    public JSONHelper setObject(JSONArray array, int index) {
        try {
            this.object = array.getJSONObject(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    // sets the object being worked on
    public JSONHelper setObject(JSONObject object) {
        this.object = object;
        return this;
    }

    // returns a JSONHelper with the object that is in a given index of the key element
    public JSONHelper tryGetArray(String key, int index) {
        JSONArray arr = tryGetArray(key);

        if (arr.length() <= index) {
            return null;
        }

        return new JSONHelper(arr, index);
    }

    // returns a JSONArray at given key, returns empty array if not found
    public JSONArray tryGetArray(String key) {
        try {
            return object.getJSONArray(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    // returns a JSONHelper with the attribute returned by the key of the current JSONObject
    public JSONHelper tryGetObject(String key) {
        try {
            return new JSONHelper(object.getJSONObject(key));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // returns a string based on given keys
    // if first key fails, will check next until it runs out
    // if all fail then it will return an empty string (no nulls)
    public String tryGet(String... keys) {
        return tryGet(false, keys);
    }

    // returns a string based on given keys
    // if first key fails, will check next until it runs out
    // if all fail then it will return an empty string (no nulls)
    // get_integer bool checks for integer value, but still returns as string representation
    public String tryGet(boolean get_integer, String... keys) {
        return get_integer ? String.valueOf(tryGet(0, keys)) : tryGet("", keys);
    }

    // returns value mapped to key or default value if not found
    private String tryGet(String default_value, String... keys) {
        return tryGet((value, object) -> {
            try {
                return object.getString(value);
            } catch (JSONException e) {
                return null;
            }
        }, default_value, keys);
    }

    // returns value mapped to key or default value if not found
    private int tryGet(int default_value, String... keys) {
        return tryGet((value, object) -> {
            try {
                return object.getInt(value);
            } catch (JSONException e) {
                return null;
            }
        }, default_value, keys);
    }

    // returns value mapped to key or default value if not found
    private <T> T tryGet(converter<T> converter, T default_value, String... keys) {
        KEYS: for (String key : keys) { // find first value of key that exists
            JSONObject executor = object;
            String final_key = key;
            String[] sub_keys = key.split("\\."); // layers
            if (sub_keys.length > 1) {
                JSONHelper helper = tryGetObject(sub_keys[0]); // first layer
                if (helper == null) { // failed layer
                    Log.d("JSONHelper", "Failed sub_key " + sub_keys[0]);
                    continue;
                }
                for (int index = 1; index < sub_keys.length - 1; index++) {
                    helper = helper.tryGetObject(sub_keys[index]); // next layer
                    if (helper == null) { // failed layer
                        Log.d("JSONHelper", "Failed sub_key " + sub_keys[index]);
                        continue KEYS; // continue KEYS block for loop
                    }
                }
                final_key = sub_keys[sub_keys.length - 1]; // reached final layer
                executor = helper.object; // last layer JSONObject
            }
            T value = converter.execute(final_key, executor); // attempts to convert value to type of T (generic)
            if (value == null) {
                continue; // value could not successfully be converted to type of T (generic)
            }
            return value; // return value
        }
        return default_value; // failed to find values with keys, return default value
    }

    // functional interface used for type converters to type T (generic)
    @FunctionalInterface
    public interface converter<T> {
        T execute(String key, JSONObject object);
    }
}
