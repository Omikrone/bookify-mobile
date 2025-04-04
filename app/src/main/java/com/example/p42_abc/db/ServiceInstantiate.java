package com.example.p42_abc.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceInstantiate {
    private static final String BASE_URL = "https://bookify-api-jck1.onrender.com/";
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
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
