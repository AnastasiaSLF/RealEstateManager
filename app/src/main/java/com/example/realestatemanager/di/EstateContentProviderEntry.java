package com.example.realestatemanager.di;


import com.example.realestatemanager.Database;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@EntryPoint
@InstallIn(SingletonComponent.class)
public interface EstateContentProviderEntry {

    Database getDatabase();

}