package com.example.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.example.realestatemanager.modele.Property;
import com.example.realestatemanager.providers.EstateProvider;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

public class EstateRepository {
    private final EstateProvider propertyProvider;
    private final Executor doInBackground = Executors.newFixedThreadPool(2);

    @Inject
    public EstateRepository(EstateProvider propertyProvider) {
        this.propertyProvider = propertyProvider;
    }

    public LiveData<Integer> create(Property property) {
        return propertyProvider.create(property);
    }

    public LiveData<Property> getById(int id) {
        return propertyProvider.getById(id);
    }

    public LiveData<List<Property>> getAll() {
        return propertyProvider.getAll();
    }

    public void update(Property property) {
        doInBackground.execute(() -> propertyProvider.update(property));
    }

    public void delete(Property property) {
        doInBackground.execute(() -> propertyProvider.delete(property));
    }

    public void addPointOfInterestToProperty(int propertyId, int pointOfInterestId) {
        doInBackground.execute(
                () -> propertyProvider.associateWithPointOfInterest(propertyId, pointOfInterestId));
    }

    public void removePointOfInterestFromProperty(int propertyId, int pointOfInterestId) {
        doInBackground.execute(
                () -> propertyProvider.removePointOfInterestFromProperty(propertyId, pointOfInterestId));
    }
}