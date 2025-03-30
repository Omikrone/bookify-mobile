package com.example.p42_abc.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class Book {

    @SerializedName("id")
    private int _id;

    @SerializedName("title")
    private String _title;

    @SerializedName("publicationYear")
    private int _publicationYear;

    @SerializedName("cover")
    private String _cover;

    @SerializedName("description")
    private String _description;

    @SerializedName("authorId")
    private int _authorId;

    @SerializedName("avgRating")
    private int _avgRating;

    @SerializedName("_count")
    private Count _count;

    // Constructor
    public Book(int id, String title, int publicationYear, String cover, String description,
                int authorId, int avgRating, Count count) {
        this._id = id;
        this._title = title;
        this._publicationYear = publicationYear;
        this._cover = cover;
        this._description = description;
        this._authorId = authorId;
        this._avgRating = avgRating;
        this._count = count;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    public int getPublicationYear() {
        return _publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this._publicationYear = publicationYear;
    }

    public String getCover() {
        return _cover;
    }

    public void setCover(String cover) {
        this._cover = cover;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public int getAuthorId() {
        return _authorId;
    }

    public void setAuthorId(int authorId) {
        this._authorId = authorId;
    }

    public int getAvgRating() {
        return _avgRating;
    }

    public void setAvgRating(int avgRating) {
        this._avgRating = avgRating;
    }

    public Count getCount() {
        return _count;
    }

    public void setCount(Count count) {
        this._count = count;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + _id +
                ", title='" + _title + '\'' +
                ", publicationYear=" + _publicationYear +
                ", cover='" + _cover + '\'' +
                ", description='" + _description + '\'' +
                ", authorId=" + _authorId +
                ", avgRating=" + _avgRating +
                ", count=" + _count +
                '}';
    }
}