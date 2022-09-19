package com.example.realestatemanager;

import static com.example.realestatemanager.DummyData.fakeProperty;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.realestatemanager.ViewModel.EstateListViewModel;
import com.example.realestatemanager.modele.Property;
import com.example.realestatemanager.providers.EstateProvider;
import com.example.realestatemanager.repositories.EstateRepository;
import com.example.realestatemanager.repositories.PointOfInterestRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

public class EstateViewModelTest {
    private EstateProvider propertyProvider;
    private EstateListViewModel estateListViewModel;
    private EstateRepository estateRepository;
    private PointOfInterestRepository pointOfInterestRepository;

    private MutableLiveData<List<Property>> allPropertiesTest = new MutableLiveData<>();
    private LiveData<List<Property.PointOfInterest>> allPointOfInterestTest;
    private EstateListViewModel listViewModel;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        estateRepository = mock(EstateRepository.class);
        pointOfInterestRepository = mock(PointOfInterestRepository.class);
        propertyProvider = mock(EstateProvider.class);
        setUpMocks();

    }

    private void setUpMocks() {
        when(propertyProvider.getById(fakeProperty.getId())).thenReturn(new MutableLiveData<>(fakeProperty));
        when(propertyProvider.getAll()).thenReturn(new MutableLiveData<>(Collections.singletonList(fakeProperty)));
    }

    @Test
    public void getProperties() throws InterruptedException {
        allPropertiesTest.setValue(DummyData.generateListPropertiesTest());

        Mockito.when(estateRepository.getAll())
                .thenReturn(allPropertiesTest);

        List<Property> propertyList = LiveDataUtils
                .getLiveDataValue(estateRepository.getAll());

        Assert.assertEquals(Property.Type.HOUSE, propertyList.get(0).getType());
        Assert.assertEquals("description1", propertyList.get(0).getDescription());
        Assert.assertEquals(Collections.singletonList(new Property.PointOfInterest("name")), propertyList.get(0).getPointOfInterestNearby());
        Assert.assertEquals(false, propertyList.get(0).isSold());
    }

}
