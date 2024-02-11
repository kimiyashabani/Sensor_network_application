package com.example.sensor_network_application.rest;


import com.google.gson.JsonObject;

import retrofit2.*;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface RestRequests {

    //request to get the fields values
    @Headers({"Accept: application/json"})
    @GET("feeds.json?api_key=82LN0KO0H75INS69&results=2")
    Call<JsonObject> getValues();


}
