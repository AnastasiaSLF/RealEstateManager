package com.example.realestatemanager.providers;

import androidx.lifecycle.LiveData;

import com.example.realestatemanager.modele.Photo;

import java.util.List;

public interface PhotoProvider {
    LiveData<List<Photo>> getByPropertyId(int propertyId);

    void update(Photo photo);

    void delete(Photo photo);

    void create(Photo... photo);
}
