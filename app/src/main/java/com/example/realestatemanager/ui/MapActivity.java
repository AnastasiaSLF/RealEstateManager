package com.example.realestatemanager.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.example.realestatemanager.R;
import com.example.realestatemanager.ViewModel.EstateListViewModel;
import com.example.realestatemanager.dialog.EstateFilterDialog;
import com.example.realestatemanager.modele.Property;
import com.example.realestatemanager.modele.geocodingAPI.Geocoding;
import com.example.realestatemanager.providers.LocationProvider;
import com.example.realestatemanager.utils.EstateManagerStream;
import com.example.realestatemanager.utils.LocationUtil;
import com.example.realestatemanager.utils.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import pub.devrel.easypermissions.EasyPermissions;

@AndroidEntryPoint
public class MapActivity extends AppCompatActivity {

    @Inject public LocationUtil locationUtil;
    @Inject public LocationProvider locationProvider;
    @Inject public LocationPermission locationPermission;

    GoogleMap map;
    CameraUpdate currentLocationCamera;

    EstateListViewModel viewModel;
    EstateFilterDialog estateFilterDialog;

    List<Property> propertyList;
    List<Property.PointOfInterest> pointOfInterestList;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private List<Property> estateList = new ArrayList<>();
    private List<String> addressList = new ArrayList<>();

    private int id;
    private Property.Type estateType;
    private String completeAddress;
    private List<Integer> idList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        final SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::configureMap);
        configureActionsButtons();
        viewModel = new ViewModelProvider(this).get(EstateListViewModel.class);
        viewModel
                .getAllPointOfInterest()
                .observe(this, pointOfInterests -> pointOfInterestList = pointOfInterests);
        viewModel.getProperties().observe(this, this::createStringForAddress);

    }

    @SuppressLint("MissingPermission")
    private void configureMap(GoogleMap map) {
        this.map = map;
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setOnInfoWindowClickListener(this::onInfoWindowClickListener);

        locationProvider.getCurrentCoordinates(
                location -> {
                    map.setMyLocationEnabled(true);
                    currentLocationCamera =
                            CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(location.getLatitude(), location.getLongitude()), 14);
                    map.animateCamera(currentLocationCamera);
                    viewModel
                            .getProperties()
                            .observe(
                                    this,
                                    properties -> {
                                        propertyList = properties;
                                        createStringForAddress(properties);
                                    });
                });
    }

    private void onInfoWindowClickListener(Marker marker) {

        final Bundle detailActivityArgs = new Bundle();
        long estateId = Long.parseLong(Objects.requireNonNull(marker.getTag()).toString());
        detailActivityArgs.putInt(EstateDetailActivity.PROPERTY_ID_ARG_KEY, ( int ) estateId);
        final Intent detailActivityIntent = new Intent(this, EstateDetailActivity.class);
        detailActivityIntent.putExtras(detailActivityArgs);
        startActivity(detailActivityIntent);
    }


    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, locationPermission);
    }

    private void configureActionsButtons() {
        findViewById(R.id.backButton).setOnClickListener(__ -> finish());
        findViewById(R.id.myLocationButton)
                .setOnClickListener(__ -> map.animateCamera(currentLocationCamera));

    }


    public void createStringForAddress(List<Property> estateList) {
        this.estateList = estateList;
        if (!Objects.requireNonNull(estateList).isEmpty()) {
            for (Property est : estateList) {
                id = est.getId();
                estateType = est.getType();
                Property.Address address = est.getAddress();
                String city = est.getAddress().getLocality();
                completeAddress = address + ","  + city;

                addressList.addAll(Collections.singleton(completeAddress));
                idList.add(id);

                Log.d("addressList", "addressList" + addressList);

                Log.d("createString", "createString" + completeAddress);

            }
            if (Utils.isInternetAvailable(Objects.requireNonNull(this))) {
                executeHttpRequestWithRetrofit();
            } else {
                Snackbar.make(findViewById(R.id.map), "No internet available", Snackbar.LENGTH_SHORT).show();

            }
        }
    }

    //http request for geocoding
    private void executeHttpRequestWithRetrofit() {
        for (String address : addressList) {
            Disposable d = EstateManagerStream.streamFetchGeocode(address)
                    .subscribeWith(new DisposableObserver<Geocoding>() {
                        @Override
                        public void onNext(Geocoding geocoding) {

                            LatLng latLng = new LatLng(geocoding.getResults().get(0).getGeometry()
                                    .getLocation().getLat(), geocoding.getResults().get(0).getGeometry()
                                    .getLocation().getLng());

                            Marker marker = map.addMarker(new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                                    .title(geocoding.getResults().get(0).getFormattedAddress()));

                            marker.setTag(idList.get(addressList.indexOf(address)));


                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }
                    });
            mCompositeDisposable.add(d);
        }
    }
}




