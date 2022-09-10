package com.example.realestatemanager.di;

import com.example.realestatemanager.adapters.PhotoDataProvider;
import com.example.realestatemanager.adapters.PointOfInterestDataProvider;
import com.example.realestatemanager.adapters.PropertyDataProvider;
import com.example.realestatemanager.adapters.RealEstateAgentDataProvider;
import com.example.realestatemanager.providers.EstateProvider;
import com.example.realestatemanager.providers.PhotoProvider;
import com.example.realestatemanager.providers.PointOfInterestProvider;
import com.example.realestatemanager.providers.RealEstateAgentProvider;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class SingletonModuleInterface {

    @Binds
    @Singleton
    public abstract EstateProvider bindPropertyProvider(PropertyDataProvider propertyDataProvider);

    @Binds
    @Singleton
    public abstract PhotoProvider bindPropertyPhotoProvider(PhotoDataProvider photoDataProvider);

    @Binds
    @Singleton
    public abstract RealEstateAgentProvider bindAgentProvider(
            RealEstateAgentDataProvider realEstateAgentDataProvider);

    @Binds
    @Singleton
    public abstract PointOfInterestProvider bingPointOfInterestDataProvider(
            PointOfInterestDataProvider pointOfInterestDataProvider);
}
