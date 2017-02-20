package com.example.yuchi.issuepro.comment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.yuchi.issuepro.R;
import com.example.yuchi.issuepro.model.Comment;

import java.util.ArrayList;

public class CommentDialog extends AppCompatDialogFragment {

    private ArrayList<Comment> commentList = new ArrayList<>();
    private CommentController commentController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);

        if (getArguments() != null) {
            commentList = getArguments().getParcelableArrayList("comment");
        }

        commentController = new CommentController(getActivity());
    }

    public static CommentDialog newInstance(ArrayList<Comment> commentList) {
        CommentDialog commentDialog = new CommentDialog();
        Bundle bundle = new Bundle();
        if (commentList != null) {
            bundle.putParcelableArrayList("comment", commentList);
        }
        commentDialog.setArguments(bundle);
        return commentDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.comment_root_view, container,
                false);
        initializeClickButtons(rootView);

        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.comments);
        commentController.setRootView(layout);
        commentController.updateComments(commentList);

        setCancelable(true);
        return rootView;
    }

    private void initializeClickButtons(@NonNull View view) {
        View buttonClose = view.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
