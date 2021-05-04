package com.example.newnormal.util;

import com.example.newnormal.data.models.TravelAdvisory;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TravelAdvisoryApi {
    @GET("/api")
    Call<TravelAdvisory> getTravelAdvisory();
}