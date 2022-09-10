package com.example.realestatemanager.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.example.realestatemanager.entities.EstateEntity;

import java.util.List;

@Dao
public interface EstateDao {

    @Query("SELECT * FROM property WHERE property_id=:id")
    EstateEntity getById(int id);

    @Query("SELECT * FROM property WHERE property_id=:id")
    Cursor getAsCursor(int id);

    @Query("SELECT * FROM property")
    List<EstateEntity> getAll();

    @Update
    void update(EstateEntity property);

    @Delete
    void delete(EstateEntity property);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] create(EstateEntity... property);

}