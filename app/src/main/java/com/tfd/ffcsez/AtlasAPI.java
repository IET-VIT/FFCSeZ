package com.tfd.ffcsez;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AtlasAPI {

    String BASE_URL = "https://ffcsez.herokuapp.com/";
    @GET("course")

    Call<AtlasModel> getResult();
}
