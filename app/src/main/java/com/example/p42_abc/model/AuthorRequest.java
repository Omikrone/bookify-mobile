package com.example.p42_abc.model;

import com.google.gson.annotations.SerializedName;
public class AuthorRequest {

    @SerializedName("firstname")
    private String _firstname;

    @SerializedName("lastname")
    private String _lastname;

    @SerializedName("image")
    private String _image;

    @SerializedName("bio")
    private String _bio;

    @SerializedName("birthDate")
    private String _birthDate;

    @SerializedName("deathDate")
    private String _deathDate;

    // Constructor
    public AuthorRequest(String firstname, String lastname, String image, String bio, String birthDate, String deathDate) {
        this._firstname = firstname;
        this._lastname = lastname;
        this._image = image;
        this._bio = bio;
        this._birthDate = birthDate;
        this._deathDate = deathDate;
    }
}
