package com.example.realestatemanager.providers;


import androidx.lifecycle.LiveData;

import com.example.realestatemanager.modele.Property;

import java.util.List;

public interface EstateProvider {

    LiveData<Property> getById(int id);
    LiveData<List<Property>> getAll();
    void update(Property property);
    void delete(Property property);
    LiveData<Integer> create(Property property);
    void associateWithPointOfInterest(int propertyId, int pointOfInterestId);
    void removePointOfInterestFromProperty(int propertyId, int pointOfInterestId);
}
