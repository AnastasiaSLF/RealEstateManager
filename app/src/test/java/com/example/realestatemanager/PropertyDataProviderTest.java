package com.example.realestatemanager;

import static com.example.realestatemanager.DummyData.fakeAgentEntity;
import static com.example.realestatemanager.DummyData.fakePointOfInterestEntity;
import static com.example.realestatemanager.DummyData.fakeProperty;
import static com.example.realestatemanager.DummyData.fakePropertyEntity;
import static com.example.realestatemanager.DummyData.fakePropertyPhotoEntity;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.ArgumentCaptor;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.realestatemanager.adapters.PropertyDataProvider;
import com.example.realestatemanager.dao.EstateDao;
import com.example.realestatemanager.dao.PhotoDao;
import com.example.realestatemanager.dao.PointOfInterestDao;
import com.example.realestatemanager.dao.PropertyPointOfInterestCrossRefDao;
import com.example.realestatemanager.dao.RealEstateAgentDao;
import com.example.realestatemanager.entities.EstateEntity;
import com.example.realestatemanager.entities.Relationships;
import com.example.realestatemanager.modele.Property;

public class PropertyDataProviderTest {

    private RealEstateAgentDao realEstateAgentDao;
    private EstateDao propertyDao;
    private PhotoDao photoDao;
    private PointOfInterestDao pointOfInterestDao;
    private PropertyDataProvider propertyDataProvider;
    private PropertyPointOfInterestCrossRefDao propertyPointOfInterestCrossRefDao;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        realEstateAgentDao = mock(RealEstateAgentDao.class);
        propertyDao = mock(EstateDao.class);
        photoDao = mock(PhotoDao.class);
        pointOfInterestDao = mock(PointOfInterestDao.class);
        propertyPointOfInterestCrossRefDao = mock(PropertyPointOfInterestCrossRefDao.class);

        propertyDataProvider =
                new PropertyDataProvider(realEstateAgentDao, propertyDao, photoDao, pointOfInterestDao, propertyPointOfInterestCrossRefDao);

        setUpMocks();
    }

    private void setUpMocks() {
        when(propertyDao.getById(fakePropertyEntity.getId())).thenReturn(fakePropertyEntity);
        when(realEstateAgentDao.getById(fakeAgentEntity.getId())).thenReturn(fakeAgentEntity);
        when(pointOfInterestDao.getPointOfInterestByPropertyId(fakePropertyEntity.getId()))
                .thenReturn(new MutableLiveData<>(Collections.singletonList(fakePointOfInterestEntity)));
        when(photoDao.getByPropertyId(fakePropertyEntity.getId()))
                .thenReturn(new MutableLiveData<>(Collections.singletonList(fakePropertyPhotoEntity)));

        final Relationships.PropertyWithPhotosAndPointOfInterest propertyWithPhotoAndPointOfInterest =
                new Relationships.PropertyWithPhotosAndPointOfInterest();
        propertyWithPhotoAndPointOfInterest.property = fakePropertyEntity;
        propertyWithPhotoAndPointOfInterest.photos = Collections.singletonList(fakePropertyPhotoEntity);
        propertyWithPhotoAndPointOfInterest.pointOfInterests =
                Collections.singletonList(fakePointOfInterestEntity);

        final Relationships.RealEstateAgentWithProperties agentWithProperties = new Relationships.RealEstateAgentWithProperties();
        agentWithProperties.realEstateAgent = fakeAgentEntity;
        agentWithProperties.tempProperties =
                Collections.singletonList(propertyWithPhotoAndPointOfInterest);
        when(realEstateAgentDao.getAllAgentWithProperties())
                .thenReturn(new MutableLiveData<>(Collections.singletonList(agentWithProperties)));
    }

    @Test
    public void getPropertyById() throws InterruptedException {
        final Property property = LiveDataUtils.getLiveDataValue(propertyDataProvider.getById(fakeProperty.getId()));

        assertEquals(property.getAgent(), fakeAgentEntity);
        assertTrue(property.getPhotoList().contains(fakePropertyPhotoEntity));
        assertTrue(property.getPointOfInterestNearby().contains(fakePointOfInterestEntity));
    }

    @Test
    public void getAllProperties() throws InterruptedException {
        final Property property = LiveDataUtils.getLiveDataValue(propertyDataProvider.getAll()).get(0);

        assertEquals(property.getPhotoList().get(0).getUrl(), fakePropertyPhotoEntity.getUrl());
        assertEquals(
                property.getPointOfInterestNearby().get(0).getName(), fakePointOfInterestEntity.getName());
        assertEquals(property.getAgent().getName(), fakeAgentEntity.getName());
    }

    @Test
    public void update() {
        propertyDataProvider.update(fakeProperty);

        final ArgumentCaptor<EstateEntity> propertyCaptor =
                ArgumentCaptor.forClass(EstateEntity.class);
        verify(propertyDao).update(propertyCaptor.capture());

        final EstateEntity capturedArg = propertyCaptor.getValue();

        assertEquals(capturedArg.id, fakeProperty.getId());
        assertEquals(capturedArg.getDescription(), fakeProperty.getDescription());
        assertEquals(capturedArg.getPrice(), fakeProperty.getPrice(), 0);
    }

    @Test
    public void delete() {
        propertyDataProvider.delete(fakePropertyEntity);
        final ArgumentCaptor<EstateEntity> propertyCaptor =
                ArgumentCaptor.forClass(EstateEntity.class);
        verify(propertyDao).delete(propertyCaptor.capture());

        final EstateEntity capturedArg = propertyCaptor.getValue();

        assertEquals(capturedArg.id, fakeProperty.getId());
        assertEquals(capturedArg.getDescription(), fakeProperty.getDescription());
        assertEquals(capturedArg.getPrice(), fakeProperty.getPrice(), 0);
    }

    @Test
    public void create() {
        when(propertyDao.create(any())).thenReturn(new long[1]);
        propertyDataProvider.create(fakePropertyEntity);

        final ArgumentCaptor<EstateEntity> propertyCaptor =
                ArgumentCaptor.forClass(EstateEntity.class);
        verify(propertyDao).create(propertyCaptor.capture());

        final EstateEntity capturedArg = propertyCaptor.getValue();

        assertEquals(capturedArg.id, fakeProperty.getId());
        assertEquals(capturedArg.getDescription(), fakeProperty.getDescription());
        assertEquals(capturedArg.getPrice(), fakeProperty.getPrice(), 0);
    }
}