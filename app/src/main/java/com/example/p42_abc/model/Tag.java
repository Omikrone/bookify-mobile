package com.example.p42_abc.model;

import com.google.gson.annotations.SerializedName;

public class Tag {

    @SerializedName("id")
    private int _id;

    @SerializedName("name")
    private String _name;

    @SerializedName("color")
    private String _color;

    // Constructeu
    public Tag(int id, String name, String color) {
        this._id = id;
        this._name = name;
        this._color = color;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getColor() {
        return _color;
    }

    public void setColor(String color) {
        this._color = color;
    }
}
