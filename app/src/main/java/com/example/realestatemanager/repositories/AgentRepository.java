package com.example.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.example.realestatemanager.modele.RealEstateAgent;
import com.example.realestatemanager.providers.RealEstateAgentProvider;

import java.util.List;

import javax.inject.Inject;

public class AgentRepository {
private final RealEstateAgentProvider realEstateAgentProvider;

    @Inject
    public AgentRepository(RealEstateAgentProvider realEstateAgentProvider) {
        this.realEstateAgentProvider = realEstateAgentProvider;
    }

    public LiveData<List<RealEstateAgent>> getAll(){
        return realEstateAgentProvider.getAll();
    }

}
