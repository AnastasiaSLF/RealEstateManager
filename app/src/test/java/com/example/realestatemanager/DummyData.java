package com.example.realestatemanager;


import com.example.realestatemanager.entities.EstateEntity;
import com.example.realestatemanager.entities.PhotoEntity;
import com.example.realestatemanager.entities.PointOfInterestEntity;
import com.example.realestatemanager.entities.RealEstateAgentEntity;
import com.example.realestatemanager.modele.Photo;
import com.example.realestatemanager.modele.Property;
import com.example.realestatemanager.modele.RealEstateAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface DummyData {

    Photo fakePhoto = new Photo("url", "desc");
    RealEstateAgent fakeAgent = new RealEstateAgent("name");
    Property.Address fakeAddress = new Property.Address("locality", "postalCode", "formattedAddr");
    Property fakeProperty =
            new Property(
                    Property.Type.HOUSE,
                    2.5,
                    54.56,
                    5,
                    "desc",
                    Collections.singletonList(fakePhoto),
                    fakeAddress,
                    Collections.singletonList(new Property.PointOfInterest("name")),
                    false,
                    464,
                    3254,
                    fakeAgent);


    EstateEntity.AddressEntity fakeAddressEntity =
            new EstateEntity.AddressEntity("locality", "postalCode", "formattedAddr");
    EstateEntity fakePropertyEntity =
            new EstateEntity(0,
                    Property.Type.APARTMENT, 2.5, 54.56, 5, "desc", fakeAddressEntity, false, 464, 3254, "mainPhotoUrl");
    PhotoEntity fakePropertyPhotoEntity = new PhotoEntity("url", "d");
    PointOfInterestEntity fakePointOfInterestEntity = new PointOfInterestEntity("poi");
    RealEstateAgentEntity fakeAgentEntity =
            new RealEstateAgentEntity("Agent");

    Property dummyProperty = new Property(
            Property.Type.HOUSE,
            3.5,
            64.56,
            3,
            "desc",
            Collections.singletonList(fakePhoto),
            fakeAddress,
            Collections.singletonList(new Property.PointOfInterest("name")),
            false,
            464,
            3254,
            fakeAgent);


    public static List<Property> generateListPropertiesTest() {
        List<Property> listProperties = new ArrayList<>();
        listProperties.add(new Property(Property.Type.HOUSE,
                2.5,
                99.45,
                5,
                "description1",
                Collections.singletonList(fakePhoto),
                fakeAddress,
                Collections.singletonList(new Property.PointOfInterest("name")),
                false,
                464,
                3254,
                fakeAgent));
        listProperties.add(new Property(Property.Type.APARTMENT,
                1.5,
                80.30,
                3,
                "description2",
                Collections.singletonList(fakePhoto),
                fakeAddress,
                Collections.singletonList(new Property.PointOfInterest("name")),
                false,
                456,
                3456,
                fakeAgent));
        listProperties.add(new Property(Property.Type.MANOR,
                1.1,
                480.30,
                15,
                "description2",
                Collections.singletonList(fakePhoto),
                fakeAddress,
                Collections.singletonList(new Property.PointOfInterest("name")),
                false,
                432,
                3213,
                fakeAgent));

        return listProperties;


    }

}
