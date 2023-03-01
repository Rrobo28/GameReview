package com.roberttayler.gamereviewapp.comments;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.roberttayler.gamereviewapp.FireStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// represents comment object
public class Comment {
    public final long gameid;
    public final long rating;
    public final String content;
    public final Timestamp date;
    public final User user;

    // creates comment from given map
    public Comment(Map<String, Object> map) {
        this((long) map.get("gameid"), (long) map.get("rating"), new User((Map<String, Object>) map.get("user")), (String) map.get("content"), (Timestamp) map.get("date"));
    }

    public Comment(long gameid, long rating, User user, String content, Timestamp date) {
        this.gameid = gameid;
        this.rating = rating;
        this.content = content;
        this.user = user;
        this.date = date;
    }

    // uploads this comment to the firestore database
    public Map<String, Object> upload() {
        Map<String, Object> comment = new HashMap<String, Object>();
        comment.put("content", content);
        comment.put("date", date);
        comment.put("gameid", gameid);
        comment.put("rating", rating);
        comment.put("user", user);

        FireStore.DATABASE.collection(String.valueOf(gameid)).add(comment);
        return comment;
    }

    // gets all the comments with gameid and allows you to execute an action on them
    public static void withComments(long gameid, action action) {
        FireStore.DATABASE.collection(String.valueOf(gameid)).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                return;
            }
            List<Comment> comments = new ArrayList<Comment>();
            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                if (document == null) { continue; }
                comments.add(new Comment(document.getData()));
            }
            action.execute(comments);
        });
    }

    // interface used for comments action
    @FunctionalInterface
    public interface action {
        void execute(List<Comment> comments);
    }
}
