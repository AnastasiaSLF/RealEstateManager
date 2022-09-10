package com.example.realestatemanager.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.realestatemanager.modele.RealEstateAgent;


@Entity(tableName = "real_estate_agent")
public class RealEstateAgentEntity extends RealEstateAgent {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "agent_id")
    public int id;


    public RealEstateAgent toModel(){
        final RealEstateAgent model = new RealEstateAgent(getName());
        model.setId(id);
        return model;
    }

    public RealEstateAgentEntity(String name) {
        super(name);
    }
}
