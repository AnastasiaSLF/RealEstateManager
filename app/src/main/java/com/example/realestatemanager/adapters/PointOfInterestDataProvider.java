package com.example.realestatemanager.adapters;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;


import com.example.realestatemanager.dao.PointOfInterestDao;
import com.example.realestatemanager.entities.PointOfInterestEntity;
import com.example.realestatemanager.modele.Property;
import com.example.realestatemanager.modele.Property.PointOfInterest;

import com.example.realestatemanager.providers.PointOfInterestProvider;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class PointOfInterestDataProvider implements PointOfInterestProvider {

    private final PointOfInterestDao pointOfInterestDao;

    public PointOfInterestDataProvider(PointOfInterestDao pointOfInterestDao) {
        this.pointOfInterestDao = pointOfInterestDao;
    }

    @Override
    public LiveData<List<PointOfInterest>> getPointOfInterestByPropertyId(int propertyId) {
        return Transformations.map(
                pointOfInterestDao.getPointOfInterestByPropertyId(propertyId), this::toModels);
    }

    @Override
    public LiveData<Integer> create(PointOfInterest pointOfInterest) {
        final MutableLiveData<Integer> liveId = new MutableLiveData<>();
        Executors.newSingleThreadExecutor()
                .execute(
                        () -> {
                            liveId.postValue(
                                    (int) pointOfInterestDao.create(new PointOfInterestEntity(pointOfInterest))[0]);
                        });
        return liveId;
    }

    @Override
    public LiveData<List<PointOfInterest>> getAll() {
        return Transformations.map(pointOfInterestDao.getAll(), this::toModels);
    }

    @Override
    public void delete(PointOfInterest pointOfInterest) {
        pointOfInterestDao.delete(new PointOfInterestEntity(pointOfInterest));
    }

    @Override
    public void update(PointOfInterest pointOfInterest) {
        pointOfInterestDao.update(new PointOfInterestEntity(pointOfInterest));
    }

    private List<PointOfInterest> toModels(List<PointOfInterestEntity> entities) {
        return entities.stream().map(PointOfInterestEntity::toModel).collect(Collectors.toList());
    }
}
