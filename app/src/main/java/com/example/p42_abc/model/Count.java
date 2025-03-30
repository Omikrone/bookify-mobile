package com.example.p42_abc.model;

import com.google.gson.annotations.SerializedName;

public class Count {
    @SerializedName("tags")
    private int _tags;

    @SerializedName("comments")
    private int _comments;

    @SerializedName("rating")
    private int _rating;

    public Count(int tags, int comments, int rating) {
        this._tags = tags;
        this._comments = comments;
        this._rating = rating;
    }

    public int getTags() {
        return _tags;
    }

    public void setTags(int tags) {
        this._tags = tags;
    }

    public int getComments() {
        return _comments;
    }

    public void setComments(int comments) {
        this._comments = comments;
    }

    public int getRating() {
        return _rating;
    }

    public void setRating(int rating) {
        this._rating = rating;
    }

    @Override
    public String toString() {
        return "Count{" +
                "tags=" + _tags +
                ", comments=" + _comments +
                ", rating=" + _rating +
                '}';
    }
}
