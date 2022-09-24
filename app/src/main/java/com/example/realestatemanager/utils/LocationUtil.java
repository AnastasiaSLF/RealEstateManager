package com.example.realestatemanager.utils;


import android.content.Context;
import android.location.Geocoder;

import com.example.realestatemanager.BuildConfig;

import java.util.Locale;
import javax.inject.Inject;
import dagger.hilt.android.qualifiers.ActivityContext;

public class LocationUtil {
    private static final String STATIC_MAP_BASE_URL =
            "https://maps.googleapis.com/maps/api/staticmap?key=" + BuildConfig.GOOGLE_MAP_STATIC_API_KEY;
    private final Geocoder geocoder;

    @Inject
    public LocationUtil(@ActivityContext Context context) {
        this.geocoder = new Geocoder(context, Locale.getDefault());
    }

    public String getLocationStaticMapFromAddress(String address) {
        return STATIC_MAP_BASE_URL + "&size=200x200&zoom=14&markers=color:red|" + address;
    }

}
