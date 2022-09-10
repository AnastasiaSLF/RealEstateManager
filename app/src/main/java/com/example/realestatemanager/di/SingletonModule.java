package com.example.realestatemanager.di;

import android.content.Context;

import com.example.realestatemanager.Database;
import com.example.realestatemanager.adapters.PhotoDataProvider;
import com.example.realestatemanager.adapters.PointOfInterestDataProvider;
import com.example.realestatemanager.adapters.PropertyDataProvider;
import com.example.realestatemanager.adapters.RealEstateAgentDataProvider;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class SingletonModule {

    @Provides
    @Singleton
    public Database provideDatabase(@ApplicationContext Context applicationContext) {
        return Database.getInstance(applicationContext);
    }

    @Provides
    @Singleton
    public PropertyDataProvider providePropertyDataProvider(@NotNull Database database) {
        return new PropertyDataProvider(
                database.getRealEstateAgentDao(),
                database.getPropertyDao(),
                database.getPhotoDao(),
                database.getPointOfInterestDao(),
                database.getPropertyPointOfInterestCrossRefDao());
    }

    @Provides
    @Singleton
    public PhotoDataProvider providePhotoDataProvider(@NotNull Database database) {
        return new PhotoDataProvider(database.getPhotoDao());
    }

    @Provides
    @Singleton
    public PointOfInterestDataProvider providePointOfInterestDataProvider(
            @NotNull Database database) {
        return new PointOfInterestDataProvider(database.getPointOfInterestDao());
    }

    @Provides
    @Singleton
    public RealEstateAgentDataProvider provideAgentDataProvider(@NotNull Database database) {
        return new RealEstateAgentDataProvider(database.getRealEstateAgentDao());
    }
}
