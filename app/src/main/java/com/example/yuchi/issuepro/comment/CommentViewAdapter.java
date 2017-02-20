package com.example.yuchi.issuepro.comment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yuchi.issuepro.R;
import com.example.yuchi.issuepro.model.Comment;

import java.util.ArrayList;
import java.util.List;

class CommentViewAdapter extends RecyclerView.Adapter<CommentViewAdapter.ViewHolder> {

    @NonNull private List<Comment> comments = new ArrayList<>();

    class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull private TextView user;
        @NonNull private TextView body;

        ViewHolder(@NonNull View view) {
            super(view);

            this.user = (TextView) view.findViewById(R.id.user);
            this.body = (TextView) view.findViewById(R.id.body);
        }
    }

    @Override
    public CommentViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View View = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
        return new CommentViewAdapter.ViewHolder(View);
    }

    @Override
    public void onBindViewHolder(final CommentViewAdapter.ViewHolder viewHolder, int position) {
        final Comment comment = comments.get(position);
        viewHolder.user.setText(comment.getUser());
        viewHolder.body.setText(comment.getBody());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    /**
     * @param comments a list of {@link Comment}.
     */
    void setViewModels(@NonNull List<Comment> comments) {
        int size = comments.size();
        this.comments = new ArrayList<>(comments.subList(0, size));
        notifyDataSetChanged();
    }
}
