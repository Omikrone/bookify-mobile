package com.example.p42_abc.retrofit;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class RetrofitClient {
    private static final String BASE_URL = "https://bookify-api-jck1.onrender.com/";
    private static Retrofit retrofit;

    @Provides
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    @Provides
    public static AuthorRepository provideAuthorRepository(ApiService apiService) {
        return new AuthorRepository(apiService);
    }
}
