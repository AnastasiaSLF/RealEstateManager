package com.example.realestatemanager.ui;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.realestatemanager.modele.Photo;
import com.example.realestatemanager.modele.Property;
import com.example.realestatemanager.modele.Property.PointOfInterest;
import com.example.realestatemanager.modele.RealEstateAgent;
import com.example.realestatemanager.repositories.AgentRepository;
import com.example.realestatemanager.repositories.EstateRepository;
import com.example.realestatemanager.repositories.PhotoRepository;
import com.example.realestatemanager.repositories.PointOfInterestRepository;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EditEstateViewModel extends ViewModel {

    private final EstateRepository propertyRepository;
    private final PhotoRepository photoRepository;
    private final PointOfInterestRepository pointOfInterestRepository;
    private final AgentRepository agentRepository;

    private final MutableLiveData<EstateDataBinding> estateDataBindingMutableLiveData =
            new MutableLiveData<>();
    private Property currentProperty;

    private LiveData<List<PointOfInterest>> allPointOfInterest;

    private LiveData<List<RealEstateAgent>> allAgents;

    @Inject
    public EditEstateViewModel(
            EstateRepository propertyRepository,
            PhotoRepository photoRepository,
            PointOfInterestRepository pointOfInterestRepository,
            AgentRepository agentRepository) {
        this.propertyRepository = propertyRepository;
        this.photoRepository = photoRepository;
        this.pointOfInterestRepository = pointOfInterestRepository;
        this.agentRepository = agentRepository;
    }

    public LiveData<EstateDataBinding> updateProperty(int propertyId) {

        final LiveData<Property> currentPropertyLiveData = propertyRepository.getById(propertyId);
        currentPropertyLiveData.observeForever(
                new Observer<Property>() {

                    @Override
                    public void onChanged(Property property) {
                        currentProperty = property;
                        final EstateDataBinding propertyDataBinding = new EstateDataBinding(currentProperty);
                        propertyDataBinding.setSelectedAgentPosition(currentProperty.getAgent().getId());
                        estateDataBindingMutableLiveData.setValue(propertyDataBinding);
                        currentPropertyLiveData.removeObserver(this);
                    }
                });

        return estateDataBindingMutableLiveData;
    }

    public LiveData<EstateDataBinding> createNewProperty() {
        currentProperty = new Property();
        currentProperty.setPhotoList(new ArrayList<>());
        currentProperty.setAddress(new Property.Address());
        currentProperty.setPointOfInterestNearby(new ArrayList<>());
        estateDataBindingMutableLiveData.setValue(new EstateDataBinding(currentProperty));
        return estateDataBindingMutableLiveData;
    }

    public void persist() {
        // TODO REFACTORING

        estateDataBindingMutableLiveData.getValue().apply();
        int selectedItemPos = estateDataBindingMutableLiveData.getValue().getSelectedAgentPosition();
        currentProperty.setAgent(allAgents.getValue().get(selectedItemPos));

        if (currentProperty.getMainPhotoUrl().isEmpty()) {
            currentProperty.setMainPhotoUrl(currentProperty.getPhotoList().get(0).getUrl());
        }

        Integer livePropertyId;
        if (currentProperty.getId() == 0) {
            livePropertyId = propertyRepository.create(currentProperty).getValue();
        } else {
            propertyRepository.update(currentProperty);
        }

    }

    public LiveData<List<RealEstateAgent>> getAllAgents() {
        allAgents = agentRepository.getAll();
        return allAgents;
    }

    public LiveData<List<PointOfInterest>> getAllPointsOfInterests() {
        if (allPointOfInterest == null) {
            allPointOfInterest = pointOfInterestRepository.getAll();
        }
        return allPointOfInterest;
    }

    public void addPointOfInterestToCurrentProperty(PointOfInterest pointOfInterest) {
        currentProperty.getPointOfInterestNearby().add(pointOfInterest);
    }

    public void removePointOrInterestFromCurrentProperty(PointOfInterest pointOfInterest) {
        currentProperty.getPointOfInterestNearby().remove(pointOfInterest);
    }

    public LiveData<Integer> createPointOfInterest(PointOfInterest pointOfInterest) {
        return pointOfInterestRepository.create(pointOfInterest);
    }

    public boolean containsPointOfInterest(PointOfInterest pointOfInterest) {
        return currentProperty.getPointOfInterestNearby().contains(pointOfInterest);
    }

    public void addPhotoToCurrentProperty(Photo photo) {
        currentProperty.getPhotoList().add(photo);
    }

    public List<Photo> getPropertyPhotos() {
        return currentProperty.getPhotoList();
    }

    public void setMainPhoto(Photo photo) {
        currentProperty.setMainPhotoUrl(photo.getUrl());
    }

    public boolean isPhotoDefined() {
        return !currentProperty.getPhotoList().isEmpty();
    }

    public boolean isSoldFieldValid(){
        if(currentProperty.isSold()){
            return !estateDataBindingMutableLiveData.getValue().getFormattedSaleDate().isEmpty();
        }
        return true;
    }

}