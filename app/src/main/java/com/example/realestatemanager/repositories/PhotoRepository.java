package com.example.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.example.realestatemanager.modele.Photo;
import com.example.realestatemanager.providers.PhotoProvider;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

public class PhotoRepository {
    private final PhotoProvider photoProvider;
    private final Executor doInBackground = Executors.newSingleThreadExecutor();

    @Inject
    public PhotoRepository(PhotoProvider photoProvider){
        this.photoProvider = photoProvider;
    }


    public void create(Photo... photo){
        doInBackground.execute(() -> photoProvider.create(photo));
    }

}