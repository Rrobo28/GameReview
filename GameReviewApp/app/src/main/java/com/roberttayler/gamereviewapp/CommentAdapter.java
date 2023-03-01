package com.roberttayler.gamereviewapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.roberttayler.gamereviewapp.comments.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private final List<Comment> commentList;
    private LayoutInflater inflater;
    private Context context;

    // This class is used to display comments
    //The class take in a list of comments and binds them to a Layout and a View Holder
    // This can then be set up in an activity so that this information can be displayed in a Recycler View
    // It will bind the comment name to the name text field within the view etc.
    public CommentAdapter(Context context, int gameid){
        inflater = LayoutInflater.from(context);
        this.commentList = new ArrayList<Comment>();
        this.context = context;
        Comment.withComments(gameid, list -> {
            commentList.addAll(list);
            if (list.isEmpty()) {
                return;
            }
            notifyItemRangeInserted(0, commentList.size());
        });
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
        notifyItemInserted(commentList.size() - 1);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.comment_view,parent,false);
        return new CommentViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
            Comment comment = commentList.get(position);
            holder.commentComment.setText(comment.content);
            holder.commentName.setText(comment.user.name);

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String myDate = format.format(comment.date.toDate());
            holder.commentDate.setText("Date: "+myDate);
            holder.commentRating.setText((int)comment.rating + " / 10");
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public final TextView commentName;
        public final TextView commentRating;
        public final TextView commentComment;
        public final TextView commentDate;
        final CommentAdapter adapter;
        public ConstraintLayout constraintLayout;

        public CommentViewHolder(@NonNull View itemView, CommentAdapter adapter) {
            super(itemView);
            commentComment = itemView.findViewById(R.id.comment_comment);
            commentName = itemView.findViewById(R.id.comment_name);
            commentRating = itemView.findViewById(R.id.comment_rating);
            commentDate = itemView.findViewById(R.id.comment_date);
            this.adapter = adapter;
            this.constraintLayout = itemView.findViewById(R.id.const_layout);
        }
    }
}
