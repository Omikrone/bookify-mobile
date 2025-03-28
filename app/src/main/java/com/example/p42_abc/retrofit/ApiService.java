package com.example.p42_abc.retrofit;

import com.example.p42_abc.model.Author;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface ApiService {
    @GET("authors")
    Call<List<Author>> getAuthors();

    @POST("authors")
    Call<Author> addAuthor(@Body AuthorRequest authorRequest);
}
