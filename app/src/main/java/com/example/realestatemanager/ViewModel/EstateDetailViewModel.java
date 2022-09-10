package com.example.realestatemanager.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.realestatemanager.modele.Property;
import com.example.realestatemanager.repositories.EstateRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EstateDetailViewModel extends ViewModel {

    private final EstateRepository propertyRepository;

    @Inject
    public EstateDetailViewModel(EstateRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public LiveData<Property> getPropertyById(int propertyId) {
        return propertyRepository.getById(propertyId);
    }
}