package com.example.yuchi.issuepro.background_task;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.example.yuchi.issuepro.R;
import com.example.yuchi.issuepro.client.IssueSearcher;
import com.example.yuchi.issuepro.client.Response;
import com.example.yuchi.issuepro.comment.CommentDialog;
import com.example.yuchi.issuepro.model.Comment;
import com.example.yuchi.issuepro.model.Issue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * AsyncTask for fetch comments.
 */
public class FetchCommentTask extends AsyncTask<String, Void, Response> {

    @NonNull
    private static final String SCHEME = "https";
    @NonNull private static final String HOST = "api.github.com";
    @NonNull private static final String URL = "repos/rails/rails/issues";

    @NonNull private final List<Issue> issueList = new ArrayList<>();
    @NonNull private final Context context;
    @NonNull private final FragmentManager fragmentManager;
    @NonNull private final IssueSearcher issueSearcher;
    @NonNull private final ArrayList<Comment> commentList;

    public FetchCommentTask(@NonNull Context context, @NonNull FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.issueSearcher = new IssueSearcher();

        this.commentList = new ArrayList<>();
    }

    @Override
    @NonNull
    protected Response doInBackground(String... params) {
        String issueId = params[0];

        Uri uri = new Uri.Builder()
                .scheme(SCHEME)
                .authority(HOST)
                .path(URL)
                .appendPath(issueId)
                .appendPath("comments")
                .build();

        java.net.URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_LONG).show();
        }

        return issueSearcher.search(url);
    }

    @Override
    protected void onPostExecute(@NonNull Response response) {
        if (response.getCode() != HttpURLConnection.HTTP_OK) {
            Toast.makeText(context, "HTTP request is bad!", Toast.LENGTH_LONG).show();
            return;
        }

        JSONArray json = response.getJsonArray();
        parseComments(json);
        Collections.sort(issueList, new Comparator<Issue>() {
            @Override
            public int compare(Issue i1, Issue i2) {
                return i2.getUpdatedAt() == null
                        ? 0 : i2.getUpdatedAt().compareTo(i1.getUpdatedAt());
            }
        });

        parseComments(json);
        showCommentsForIssueDialog();
    }

    private void parseComments(@NonNull JSONArray jsonArrayResponse) {
        commentList.clear();
        for (int i = 0; i < jsonArrayResponse.length(); i++) {
            try {
                JSONObject jsonObject = jsonArrayResponse.getJSONObject(i);
                String user = jsonObject.getJSONObject("user").getString("login");
                String body = jsonObject.getString("body");

                Comment comment = new Comment.Builder()
                        .body(body)
                        .user(user)
                        .build();
                commentList.add(comment);
            } catch (JSONException e) {
                Toast.makeText(context, context.getString(R.string.error),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showCommentsForIssueDialog() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CommentDialog commentDialog = CommentDialog.newInstance(commentList);
        commentDialog.show(fragmentTransaction, "CommentDialog");
    }
}
