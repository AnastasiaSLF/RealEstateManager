package com.example.realestatemanager.utils;

import com.example.realestatemanager.modele.geocodingAPI.Geocoding;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EstateManagerStream {


    public static Observable<Geocoding> streamFetchGeocode (String address) {
        EstateManagerService estateManagerService = EstateManagerRetrofitObject.retrofit.create(EstateManagerService.class);
        return estateManagerService.getGeocode(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}
