package com.example.yuchi.issuepro.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;

public class Issue {

    private int issueId;
    @Nullable private String body;
    @Nullable private String title;
    @Nullable private Date updatedAt;

    private Issue(@NonNull Builder builder) {
        this.issueId = builder.issueId;
        this.body = builder.body;
        this.title = builder.title;
        this.updatedAt = builder.updatedAt;
    }

    @Nullable
    public String getBody() {
        return body;
    }

    public int getIssueId() {
        return issueId;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Builder to build issue object.
     */
    public static class Builder {

        private int issueId;
        @Nullable private String body;
        @Nullable private String title;
        @Nullable private Date updatedAt;

        public Issue build() {
            return new Issue(this);
        }

        public Builder body(@Nullable String body) {
            this.body = body;
            return this;
        }

        public Builder issueId(@Nullable int issueId) {
            this.issueId = issueId;
            return this;
        }

        public Builder title(@Nullable String title) {
            this.title = title;
            return this;
        }

        public Builder updateAt(@Nullable Date updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
    }
}
