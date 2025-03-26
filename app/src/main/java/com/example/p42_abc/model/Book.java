package com.example.p42_abc.model;

public class Book {
    private final String _title;
    private final String _description;

    public Book(String title, String description) {
        this._title = title;
        this._description = description;
    }

    public String getTitle() {
        return _title;
    }
    public String getDescription() {
        return _description;
    }

}
