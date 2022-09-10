package com.example.realestatemanager.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.realestatemanager.modele.Property;


@Entity(tableName = "point_of_interest")
public class PointOfInterestEntity extends Property.PointOfInterest {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "point_of_interest_id")
    public int id;

    public PointOfInterestEntity(Property.PointOfInterest parent){
        super(parent.getName());
        if(parent.getId() != 0){
            id = parent.getId();
        }
    }

    public PointOfInterestEntity(String name) {
        super(name);
    }

    public PointOfInterestEntity toModel(){
        super.setId(id);
        return this;
    }
}
