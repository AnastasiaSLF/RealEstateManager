package com.example.realestatemanager.adapters;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;


import com.example.realestatemanager.dao.EstateDao;
import com.example.realestatemanager.dao.PhotoDao;
import com.example.realestatemanager.dao.PointOfInterestDao;
import com.example.realestatemanager.dao.PropertyPointOfInterestCrossRefDao;
import com.example.realestatemanager.dao.RealEstateAgentDao;
import com.example.realestatemanager.entities.EstateEntity;
import com.example.realestatemanager.entities.PhotoEntity;
import com.example.realestatemanager.entities.PointOfInterestEntity;
import com.example.realestatemanager.entities.Relationships;
import com.example.realestatemanager.modele.Property;
import com.example.realestatemanager.providers.EstateProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class PropertyDataProvider implements EstateProvider {

    private final RealEstateAgentDao realEstateAgentDao;
    private final EstateDao propertyDao;
    private final PhotoDao photoDao;
    private final PointOfInterestDao pointOfInterestDao;
    private final PropertyPointOfInterestCrossRefDao propertyPointOfInterestCrossRefDao;

    private final Executor doInBackground = Executors.newSingleThreadExecutor();
    final private MutableLiveData<List<Property>> allProperties = new MutableLiveData<>(new ArrayList<>());
    EstateEntity propertyEntity;

    public PropertyDataProvider(
            RealEstateAgentDao realEstateAgentDao,
            EstateDao propertyDao,
            PhotoDao photoDao,
            PointOfInterestDao pointOfInterestDao,
            PropertyPointOfInterestCrossRefDao propertyPointOfInterestCrossRefDao) {
        this.realEstateAgentDao = realEstateAgentDao;
        this.propertyDao = propertyDao;
        this.photoDao = photoDao;
        this.pointOfInterestDao = pointOfInterestDao;
        this.propertyPointOfInterestCrossRefDao = propertyPointOfInterestCrossRefDao;
    }

    @Override
    public LiveData<Property> getById(int propertyId) {
        // TODO REFACTORING
        final MutableLiveData<Property> propertyLiveData = new MutableLiveData<>();
        doInBackground.execute(
                () -> {
                    propertyEntity = propertyDao.getById(propertyId);
                    propertyEntity.setAgent(realEstateAgentDao.getById(propertyEntity.agentID));
                });
        final LiveData<List<PointOfInterestEntity>> pointOfInterestsLiveData =
                pointOfInterestDao.getPointOfInterestByPropertyId(propertyId);

        pointOfInterestsLiveData.observeForever(
                new Observer<List<PointOfInterestEntity>>() {

                    @Override
                    public void onChanged(List<PointOfInterestEntity> pointOfInterestEntities) {
                        propertyEntity.setPointOfInterestNearby(
                                pointOfInterestEntities.stream()
                                        .map(PointOfInterestEntity::toModel)
                                        .collect(Collectors.toList()));
                        pointOfInterestsLiveData.removeObserver(this);

                        final LiveData<List<PhotoEntity>> photoEntitiesLiveData =
                                photoDao.getByPropertyId(propertyId);

                        photoEntitiesLiveData.observeForever(
                                new Observer<List<PhotoEntity>>() {

                                    @Override
                                    public void onChanged(List<PhotoEntity> photoEntities) {
                                        propertyEntity.setPhotoList(
                                                photoEntities.stream()
                                                        .map(PhotoEntity::toModel)
                                                        .collect(Collectors.toList()));
                                        photoEntitiesLiveData.removeObserver(this);
                                        propertyLiveData.setValue(propertyEntity);
                                    }
                                });
                    }
                });
        return propertyLiveData;
    }

    @Override
    public LiveData<List<Property>> getAll() {
        if (allProperties.getValue().isEmpty()) {
            realEstateAgentDao
                    .getAllAgentWithProperties()
                    .observeForever(
                            agentWithProperties -> {
                                final List<Property> properties = new ArrayList<>();

                                for (Relationships.RealEstateAgentWithProperties entry : agentWithProperties) {
                                    properties.addAll(entry.toProperties());
                                }

                                allProperties.postValue(properties);
                            });
        }
        return allProperties;
    }

    @Override
    public void update(Property updatedProperty) {
        propertyDao.update(new EstateEntity(updatedProperty));

        final List<Property> updatedList =
                allProperties.getValue().stream()
                        .map(
                                currentPropertyInTheList -> {
                                    if (currentPropertyInTheList.getId() == updatedProperty.getId()) {
                                        return updatedProperty;
                                    }
                                    return currentPropertyInTheList;
                                })
                        .collect(Collectors.toList());

        allProperties.postValue(updatedList);
    }

    @Override
    public void delete(Property property) {
        propertyDao.delete(new EstateEntity(property));
        allProperties.getValue().remove(property);
        allProperties.postValue(allProperties.getValue());
    }

    @Override
    public LiveData<Integer> create(Property property) {
        // TODO Refactor
        final MutableLiveData<Integer> liveId = new MutableLiveData<>();
        Executors.newSingleThreadExecutor()
                .execute(
                        () -> {
                            final EstateEntity propertyEntity = new EstateEntity(property);
                            final int id = (int) propertyDao.create(propertyEntity)[0];
                            liveId.postValue(id);
                            property.setId(id);
                            allProperties.getValue().add(property);
                            allProperties.postValue(allProperties.getValue());
                        });
        return liveId;
    }

    @Override
    public void associateWithPointOfInterest(int propertyId, int pointOfInterestId) {
        propertyPointOfInterestCrossRefDao.create(getAssociation(propertyId, pointOfInterestId));
    }

    @Override
    public void removePointOfInterestFromProperty(int propertyId, int pointOfInterestId) {
        propertyPointOfInterestCrossRefDao.delete(getAssociation(propertyId, pointOfInterestId));
    }

    private Relationships.PropertyPointOfInterestCrossRef getAssociation(
            int propertyId, int pointOfInterestId) {
        final Relationships.PropertyPointOfInterestCrossRef association =
                new Relationships.PropertyPointOfInterestCrossRef();
        association.pointOfInterestId = pointOfInterestId;
        association.propertyId = propertyId;
        return association;
    }
}
