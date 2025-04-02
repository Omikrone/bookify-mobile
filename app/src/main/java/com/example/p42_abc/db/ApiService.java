package com.example.p42_abc.db;

import com.example.p42_abc.model.Author;
import com.example.p42_abc.model.AuthorRequest;
import com.example.p42_abc.model.Book;
import com.example.p42_abc.model.BookRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface ApiService {
    @GET("authors")
    Call<List<Author>> getAuthors(@Query("page") int page, @Query("take") int take);

    @POST("authors")
    Call<Author> addAuthor(@Body AuthorRequest authorRequest);

    @DELETE("authors/{author_id}")
    Call<Author> deleteAuthor(@Path("author_id") int authorId);

    @GET("books")
    Call<List<Book>> getBooks(@Query("page") int page, @Query("take") int take);

    @POST("authors/{author_id}/books")
    Call<Book> addBook(@Path ("author_id") int authorId, @Body BookRequest bookRequest);
}
