package com.example.yuchi.issuepro.issue;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.example.yuchi.issuepro.R;
import com.example.yuchi.issuepro.model.Issue;

import java.util.List;

public class IssueController implements IssueView.Listener {

    @NonNull private Activity activity;

    private Listener listener;
    private IssueView issueView;
    private ViewGroup rootView;

    public IssueController(@NonNull Activity activity) {
        this.activity = activity;
    }

    /**
     * Set the root view to use to add and remove views into.
     *
     * @param rootView The root view.
     */
    public void setRootView(@NonNull ViewGroup rootView) {
        this.rootView = rootView;
        initIssueView();
    }

    /**
     * @param listener {@link IssueView.Listener}.
     */
    public void setListener(@NonNull Listener listener) {
        this.listener = listener;
    }

    /**
     * @param issues a list of {@link Issue}.
     */
    public void updateIssues(@NonNull List<Issue> issues) {
        issueView.setViewModels(issues);
    }

    private void initIssueView() {
         issueView = (IssueView) activity.getLayoutInflater().inflate(R.layout.issues, rootView, false);
        rootView.addView(issueView);
        issueView.setListener(this);
    }

    @Override
    public void onIssueSelected(@NonNull Issue issue) {
        listener.onIssueSelected(issue);
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
