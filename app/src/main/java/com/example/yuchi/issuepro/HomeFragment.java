package com.example.yuchi.issuepro;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yuchi.issuepro.background_task.FetchIssueTask;
import com.example.yuchi.issuepro.client.IssueSearcher;
import com.example.yuchi.issuepro.client.Response;
import com.example.yuchi.issuepro.comment.CommentDialog;
import com.example.yuchi.issuepro.issue.IssueController;
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

public class HomeFragment extends Fragment implements IssueController.Listener {

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
        FetchIssueTask fetchIssueTask = new FetchIssueTask(getActivity(), issueController);
        fetchIssueTask.execute();
    }

    @Override
    public void onIssueSelected(@NonNull Issue issue) {
        FetchCommentTask fetchCommentTask = new FetchCommentTask(getActivity());
        fetchCommentTask.execute(String.valueOf(issue.getIssueId()));
    }

    /**
     * AsyncTask for fetch open issues.
     */
    private class FetchCommentTask extends AsyncTask<String, Void, Response> {

        @NonNull private static final String SCHEME = "https";
        @NonNull private static final String HOST = "api.github.com";
        @NonNull private static final String URL = "repos/rails/rails/issues";

        @NonNull private final List<Issue> issueList = new ArrayList<>();
        @NonNull private final Context context;
        @NonNull private final IssueSearcher issueSearcher;
        @NonNull private final ArrayList<Comment> commentList;

        FetchCommentTask(@NonNull Context context) {
            this.context = context;
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
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            CommentDialog commentDialog = CommentDialog.newInstance(commentList);
            commentDialog.show(fragmentTransaction, "CommentDialog");
        }
    }
}
