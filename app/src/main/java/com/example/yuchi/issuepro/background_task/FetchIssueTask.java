package com.example.yuchi.issuepro.background_task;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.yuchi.issuepro.R;
import com.example.yuchi.issuepro.client.IssueSearcher;
import com.example.yuchi.issuepro.client.Response;
import com.example.yuchi.issuepro.issue.IssueController;
import com.example.yuchi.issuepro.model.Issue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * AsyncTask for fetch open issues.
 */
public class FetchIssueTask extends AsyncTask<String, Void, Response> {

    private static final int LIMIT_OF_CHARS = 140;
    @NonNull private static final String SCHEME = "https";
    @NonNull private static final String HOST = "api.github.com";
    @NonNull private static final String URL = "repos/rails/rails/issues";
    @NonNull private static final String STATE_PARAM = "state";
    @NonNull private static final String STATE = "open";

    @NonNull private final List<Issue> issueList = new ArrayList<>();
    @NonNull private final Context context;
    @NonNull private final IssueController issueController;
    @NonNull private final IssueSearcher issueSearcher;

    public FetchIssueTask(@NonNull Context context, @NonNull IssueController issueController) {
        this.context = context;
        this.issueController = issueController;

        this.issueSearcher = new IssueSearcher();
    }

    @Override
    @NonNull
    protected Response doInBackground(String... params) {
        Uri uri = new Uri.Builder()
                .scheme(SCHEME)
                .authority(HOST)
                .path(URL)
                .appendQueryParameter(STATE_PARAM, STATE)
                .build();

        URL url = null;
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
        parseIssueData(json);
        Collections.sort(issueList, new Comparator<Issue>() {
            @Override
            public int compare(Issue i1, Issue i2) {
                return i2.getUpdatedAt() ==  null
                        ? 0 : i2.getUpdatedAt().compareTo(i1.getUpdatedAt());
            }});

        // update the issue view
        issueController.updateIssues(issueList);
    }

    private void parseIssueData(@NonNull JSONArray jsonArrayResponse) {
        issueList.clear();
        for (int i = 0; i < jsonArrayResponse.length(); i++) {
            try {
                JSONObject jsonObject = jsonArrayResponse.getJSONObject(i);
                String title = jsonObject.getString("title");
                String body = jsonObject.getString("body");
                String updatedAt = jsonObject.getString("updated_at");
                int numOfComment = jsonObject.getInt("comments");
                int issueId = jsonObject.getInt("number");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = sdf.parse(updatedAt);

                if (!body.isEmpty() && body.length() > LIMIT_OF_CHARS) {
                    body = body.substring(0, LIMIT_OF_CHARS) + "...";
                } else {
                    body = "";
                }

                Issue issue = new Issue.Builder()
                        .title(title)
                        .body(body)
                        .updateAt(date)
                        .issueId(issueId)
                        .build();

                issueList.add(issue);
            } catch (Exception e) {
                Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_LONG).show();
            }
        }
    }
}