package com.example.realestatemanager.modele;

public class RealEstateAgent {
    private int id;
    private String name;

    public RealEstateAgent(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }



}
