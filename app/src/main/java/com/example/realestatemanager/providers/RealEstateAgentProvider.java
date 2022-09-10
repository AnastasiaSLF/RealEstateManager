package com.example.realestatemanager.providers;

import androidx.lifecycle.LiveData;

import com.example.realestatemanager.modele.RealEstateAgent;

import java.util.List;

public interface RealEstateAgentProvider {
    LiveData<List<RealEstateAgent>> getAll();
}
