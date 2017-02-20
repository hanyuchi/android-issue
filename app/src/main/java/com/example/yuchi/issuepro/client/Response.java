package com.example.yuchi.issuepro.client;

import android.support.annotation.NonNull;

import org.json.JSONArray;

public class Response {

    private final int code;
    @NonNull private final JSONArray jsonArray;

    Response(@NonNull JSONArray jsonArray, int code) {
        this.jsonArray = jsonArray;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @NonNull
    public JSONArray getJsonArray() {
        return jsonArray;
    }
}
