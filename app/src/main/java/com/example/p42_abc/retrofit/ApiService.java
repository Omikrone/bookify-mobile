package com.example.p42_abc.retrofit;

import com.example.p42_abc.model.Author;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface ApiService {
    @GET("authors")
    Call<List<Author>> getAuthors(@Query("page") int page, @Query("take") int take);

    @POST("authors")
    Call<Author> addAuthor(@Body AuthorRequest authorRequest);
}
