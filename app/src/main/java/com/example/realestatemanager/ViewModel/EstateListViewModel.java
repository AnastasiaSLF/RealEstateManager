package com.example.realestatemanager.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.realestatemanager.modele.Property;
import com.example.realestatemanager.repositories.EstateRepository;
import com.example.realestatemanager.repositories.PointOfInterestRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EstateListViewModel extends ViewModel {

    private final EstateRepository propertyRepository;
    private final PointOfInterestRepository pointOfInterestRepository;
    private LiveData<List<Property>> allProperties;
    private LiveData<List<Property.PointOfInterest>> allPointOfInterest;

    @Inject
    public EstateListViewModel(EstateRepository propertyRepository, PointOfInterestRepository pointOfInterestRepository) {
        this.propertyRepository = propertyRepository;
        this.pointOfInterestRepository = pointOfInterestRepository;
    }

    public LiveData<List<Property>> getProperties(){
        if(allProperties == null){
            allProperties = propertyRepository.getAll();
        }
        return allProperties;
    }

    public LiveData<List<Property.PointOfInterest>> getAllPointOfInterest(){
        if(allPointOfInterest == null){
            allPointOfInterest = pointOfInterestRepository.getAll();
        }
        return allPointOfInterest;
    }

}
