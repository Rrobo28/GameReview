package com.roberttayler.gamereviewapp.comments;

import java.util.Map;

// represents a user, uid and name
public class User {
    public final String uid;
    public final String name;

    public User(Map<String, Object> map) {
        this((String) map.get("uid"), (String) map.get("name"));
    }

    public User(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }
}
