package com.example.sensor_network_application.rest;


import com.google.gson.JsonObject;

import retrofit2.*;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface RestRequests {

    //request to get the fields values
    @Headers({"Accept: application/json"})
    @GET("feeds.json?api_key=X1453HODIAYZZHH3&results=1")
    Call<JsonObject> getValues();


}
