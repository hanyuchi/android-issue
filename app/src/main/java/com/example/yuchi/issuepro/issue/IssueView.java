package com.example.yuchi.issuepro.issue;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.example.yuchi.issuepro.R;
import com.example.yuchi.issuepro.model.Issue;

import java.util.List;

public class IssueView extends CardView implements IssueViewAdapter.Listener {

    private IssueViewAdapter issueViewAdapter;
    private Listener listener;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    public IssueView(Context context) {
        this(context, null);
    }

    public IssueView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IssueView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @param listener {@link Listener}.
     */
    void setListener(@NonNull Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        issueViewAdapter = new IssueViewAdapter(this);
        recyclerView.setAdapter(issueViewAdapter);
    }

    @Override
    public void onItemSelected(@NonNull Issue issue) {
        listener.onIssueSelected(issue);
    }

    /**
     * @param issues a list of {@link Issue}.
     */
    public void setViewModels(@NonNull List<Issue> issues) {
        issueViewAdapter.setViewModels(issues);
    }

    /**
     * Callback listener.
     */
    public interface Listener {

        /**
         * On item selected.
         *
         * @param issue issue view model.
         */
        void onIssueSelected(@NonNull Issue issue);
    }
}
