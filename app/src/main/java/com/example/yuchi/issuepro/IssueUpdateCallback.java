package com.example.yuchi.issuepro;

import android.support.annotation.NonNull;

import com.example.yuchi.issuepro.model.Issue;

import java.util.List;

public interface IssueUpdateCallback {

    /**
     * Update issue list.
     *
     * @param issues a list of {@link Issue}.
     */
    void updateIssueList(@NonNull List<Issue> issues);
}
