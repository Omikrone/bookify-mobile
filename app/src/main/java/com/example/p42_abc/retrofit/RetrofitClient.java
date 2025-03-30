package com.example.p42_abc.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://bookify-api-jck1.onrender.com/";
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static AuthorRepository provideAuthorRepository(ApiService apiService) {
        return new AuthorRepository(apiService);
    }

    public static BookRepository provideBookRepository(ApiService apiService) {
        return new BookRepository(apiService);
    }
}
