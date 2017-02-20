package com.example.yuchi.issuepro.comment;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.example.yuchi.issuepro.R;
import com.example.yuchi.issuepro.model.Comment;

import java.util.List;

class CommentController {

    @NonNull private Activity activity;

    private CommentView commentView;
    private ViewGroup rootView;

    CommentController(@NonNull Activity activity) {
        this.activity = activity;
    }

    /**
     * Set the root view to use to add and remove views into.
     *
     * @param rootView The root view.
     */
    void setRootView(@NonNull ViewGroup rootView) {
        this.rootView = rootView;
        initCommentView();
    }

    /**
     * @param comments a list of {@link Comment}.
     */
    void updateComments(@NonNull List<Comment> comments) {
        commentView.setViewModels(comments);
    }

    private void initCommentView() {
        commentView = (CommentView) activity.getLayoutInflater().inflate(R.layout.comments,
                rootView, false);
        rootView.addView(commentView);
    }
}
