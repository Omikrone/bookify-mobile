package com.example.p42_abc.model;

public class Author {
    private final String _firstname;
    private final String _lastname;

    public Author(String firstname, String lastname) {
        this._firstname = firstname;
        this._lastname = lastname;
    }

    public String getFirstname() {
        return _firstname;
    }
    public String getLastname() {
        return _lastname;
    }

}
