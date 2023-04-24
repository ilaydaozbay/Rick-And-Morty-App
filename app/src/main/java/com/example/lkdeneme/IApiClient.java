package com.example.lkdeneme;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IApiClient {

    @GET("location")
    Call<Location> getLocation();

    @GET
    Call<Character> getCharacter(@Url String data);


}
