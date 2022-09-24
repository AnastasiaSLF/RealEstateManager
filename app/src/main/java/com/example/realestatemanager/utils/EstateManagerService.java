package com.example.realestatemanager.utils;

import static com.example.realestatemanager.BuildConfig.GOOGLE_MAP_STATIC_API_KEY;

import com.example.realestatemanager.modele.geocodingAPI.Geocoding;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EstateManagerService {

    String GOOGLE_MAP_API_KEY = GOOGLE_MAP_STATIC_API_KEY;

    @GET("maps/api/geocode/json?key="+GOOGLE_MAP_API_KEY)
    Observable<Geocoding> getGeocode (@Query("address") String address);

}