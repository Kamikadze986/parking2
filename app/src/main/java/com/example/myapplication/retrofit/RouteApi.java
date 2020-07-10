package com.example.myapplication.retrofit;

import com.example.myapplication.MapsActivity;
import com.example.myapplication.retrofit.data.Main;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;
//Интерфейс для запросак маршрута
public interface RouteApi {
    @GET("/maps/api/directions/json")
    Call<Main> getRoute(
            @Query(value = "origin") String position,
            @Query(value = "destination") String destination,
            @Query("key") String key);
}
