package com.example.yuchi.issuepro.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Comment implements Parcelable {

    @Nullable private String body;
    @Nullable private String user;

    private Comment(@NonNull Builder builder) {
        this.user = builder.user;
        this.body = builder.body;
    }

    protected Comment(Parcel in) {
        body = in.readString();
        user = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {

        @Override
        public Comment createFromParcel(Parcel in) {
            Comment comment = new Comment(in);
            comment.user = in.readString();
            comment.body = in.readString();
            return comment;
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Nullable
    public String getBody() {
        return body;
    }

    @Nullable
    public String getUser() {
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(user);
        parcel.writeString(body);
    }

    /**
     * Builder to build comment object.
     */
    public static class Builder {

        @Nullable private String user;
        @Nullable private String body;

        public Comment build() {
            return new Comment(this);
        }

        public Builder body(@Nullable String body) {
            this.body = body;
            return this;
        }

        public Builder user(@Nullable String user) {
            this.user = user;
            return this;
        }
    }
}
