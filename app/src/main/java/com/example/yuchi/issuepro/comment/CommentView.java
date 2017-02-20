package com.example.yuchi.issuepro.comment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.yuchi.issuepro.R;
import com.example.yuchi.issuepro.model.Comment;

import java.util.List;

public class CommentView extends CardView {

    private CommentViewAdapter commentViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    public CommentView(Context context) {
        this(context, null);
    }

    public CommentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        commentViewAdapter = new CommentViewAdapter();
        recyclerView.setAdapter(commentViewAdapter);
    }

    /**
     * @param comments a list of {@link Comment}.
     */
    void setViewModels(@NonNull List<Comment> comments) {
        if (comments.isEmpty()) {
            showNoCommentText(true);
            return;
        }
        showNoCommentText(false);
        commentViewAdapter.setViewModels(comments);
    }

    private void showNoCommentText(boolean show) {
        TextView view = (TextView) findViewById(R.id.no_comment);
        if (show) {
            view.setVisibility(VISIBLE);
        } else {
            view.setVisibility(GONE);
        }
    }
}
