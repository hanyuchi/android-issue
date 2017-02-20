package com.example.yuchi.issuepro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yuchi.issuepro.background_task.FetchCommentTask;
import com.example.yuchi.issuepro.background_task.FetchIssueTask;
import com.example.yuchi.issuepro.issue.IssueController;
import com.example.yuchi.issuepro.model.Issue;

import java.util.List;

public class HomeFragment extends Fragment
        implements IssueController.Listener, IssueUpdateCallback {

    private IssueController issueController;

    public HomeFragment() { }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        issueController = new IssueController(getActivity());
        issueController.setListener(this);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        issueController.setRootView(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        FetchIssueTask fetchIssueTask = new FetchIssueTask(getActivity(), this);
        fetchIssueTask.execute();
    }

    @Override
    public void onIssueSelected(@NonNull Issue issue) {
        FetchCommentTask fetchCommentTask = new FetchCommentTask(getActivity(),
                getFragmentManager());
        fetchCommentTask.execute(String.valueOf(issue.getIssueId()));
    }

    @Override
    public void updateIssueList(@NonNull List<Issue> issues) {
        issueController.updateIssues(issues);
    }
}
