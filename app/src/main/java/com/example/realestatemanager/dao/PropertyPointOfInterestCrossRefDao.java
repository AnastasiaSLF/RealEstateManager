package com.example.realestatemanager.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.example.realestatemanager.entities.Relationships;


@Dao
public interface PropertyPointOfInterestCrossRefDao {
    @Delete
    void delete(Relationships.PropertyPointOfInterestCrossRef associationClass);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void create(Relationships.PropertyPointOfInterestCrossRef associationClass);
}
