package com.example.realestatemanager.ui;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.example.realestatemanager.R;
import com.example.realestatemanager.ViewModel.EstateListViewModel;
import com.example.realestatemanager.dialog.EstateFilterDialog;
import com.example.realestatemanager.modele.Property;
import com.example.realestatemanager.providers.LocationProvider;
import com.example.realestatemanager.utils.LocationUtil;
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

import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
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
    }

    @SuppressLint("MissingPermission")
    private void configureMap(GoogleMap map) {
        this.map = map;
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setInfoWindowAdapter(new CustomInfo(getLayoutInflater()));
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
                                        showProperties(properties);
                                    });
                });
    }

    private void onInfoWindowClickListener(Marker marker) {
        final Bundle detailActivityArgs = new Bundle();
        final int selectedPropertyId = ((Property) marker.getTag()).getId();
        detailActivityArgs.putInt(EstateDetailActivity.PROPERTY_ID_ARG_KEY, selectedPropertyId);
        final Intent detailActivityIntent = new Intent(this, EstateDetailActivity.class);
        detailActivityIntent.putExtras(detailActivityArgs);
        startActivity(detailActivityIntent);
    }

    private void showProperties(List<Property> properties) {
        map.clear();
        final LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
        final CircularProgressIndicator progressIndicator = new CircularProgressIndicator(this);
        progressIndicator.setIndeterminate(true);
        progressIndicator.show();
        Executors.newSingleThreadExecutor()
                .execute(
                        () -> {
                            for (Property currentProperty : properties) {
                                showPropertyMarker(
                                        currentProperty,
                                        locationUtil.getCoordinatesFromAddress(
                                                currentProperty.getAddress().getFormattedAddress(), bounds));
                            }
                        });
    }

    private void showPropertyMarker(Property property, LocationUtil.GeoCoordinates coordinates) {
        if (coordinates != null && property != null) {
            runOnUiThread(
                    () ->
                            map.addMarker(
                                    new MarkerOptions()
                                            .position(coordinates.toLatLng())
                                            .title(property.getType().name())
                                            .snippet(property.getAddress().getLocality())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)))
                                    .setTag(property));
        }
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
        findViewById(R.id.filterButton).setOnClickListener(this::showPropertiesFilterDialog);

    }
    public void showPropertiesFilterDialog(View __) {
        if (estateFilterDialog == null) {
            estateFilterDialog =
                    new EstateFilterDialog(propertyList, pointOfInterestList, this::showProperties);
        }
        if (!estateFilterDialog.isAdded()) {
            estateFilterDialog.show(getSupportFragmentManager(), "filter_dialog");
        }
    }
}



