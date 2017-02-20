package com.example.yuchi.issuepro.client;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IssueSearcher {

    @NonNull private JSONArray jsonResponse = new JSONArray();

    public IssueSearcher() { }

    @NonNull
    public Response search(@Nullable URL url) {
        int httpStatusCode = HttpURLConnection.HTTP_NO_CONTENT;
        Response response = new Response(jsonResponse, httpStatusCode);

        if (url == null) {
            return response;
        }

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(false);
            httpStatusCode = connection.getResponseCode();

            if (HttpURLConnection.HTTP_OK == httpStatusCode) {
                InputStream inputStream = connection.getInputStream();

                if (inputStream == null) {
                    return response;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                jsonResponse = new JSONArray(sb.toString());
            }
        } catch (Exception e) {
            Log.e("error", url + " It came in Exception catch block");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("error", url + " " + e.getMessage(), e);
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return new Response(jsonResponse, httpStatusCode);
    }
}
