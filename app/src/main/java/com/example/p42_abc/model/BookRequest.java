package com.example.p42_abc.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class BookRequest {

    @SerializedName("title")
    private String _title;

    @SerializedName("publicationYear")
    private int _publicationYear;

    @SerializedName("description")
    private String _description;

    // Constructor
    public BookRequest(String title, int publicationYear, String description) {
        this._title = title;
        this._publicationYear = publicationYear;
        this._description = description;
    }
}
