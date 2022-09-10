package com.example.realestatemanager.ui;

import static com.example.realestatemanager.modele.Property.PROPERTY_RELATED_DATE_FORMATTER;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;


import com.example.realestatemanager.BR;
import com.example.realestatemanager.modele.Property;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class EstateDataBinding extends BaseObservable {

    private final Property property;
    private String saleDate = "";
    private String publicationDate;
    private int selectedAgentPosition;

    public EstateDataBinding(Property property) {
        this.property = property;
        if (property.getPublicationDate() != 0){
            publicationDate = property.getFormattedPubDate();
        }else {
            publicationDate = getTodayDate();
        }
        if (property.getSaleDate() != 0){
            saleDate = property.getFormattedSaleDate();
        }
    }

    public void apply() {
        if(publicationDate.isEmpty()){
            publicationDate = property.getFormattedPubDate();
        }
        property.setPublicationDate(
                LocalDate.parse(publicationDate, PROPERTY_RELATED_DATE_FORMATTER).toEpochDay());
        if (property.isSold() && !saleDate.isEmpty()){
            property.setSaleDate(LocalDate.parse(saleDate, PROPERTY_RELATED_DATE_FORMATTER).toEpochDay());
        }
    }

    @Bindable
    public int getSelectedTypePosition(){
        return property.getType().ordinal();
    }

    public void setSelectedTypePosition(int position){
        property.setType(Property.Type.values()[position]);
    }

    @Bindable
    public int getSelectedAgentPosition(){
        return selectedAgentPosition;
    }

    public void setSelectedAgentPosition(int position){
        selectedAgentPosition = position;
    }

    @Bindable
    public String getPrice() {
        if(property.getPrice() == 0) return "";
        final NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setGroupingUsed(false);
        return formatter.format(property.getPrice());
    }

    public void setPrice(String price) {
        if(price.isEmpty()) {
            property.setPrice(0);
            return;
        }
        property.setPrice(Double.parseDouble(price));
    }

    @Bindable
    public String getSurface() {
        if(property.getSurface() == 0) return "";
        return String.valueOf(property.getSurface());
    }

    public void setSurface(String surface) {
        if(surface.isEmpty()) {
            property.setSurface(0);
            return;
        }
        property.setSurface(Double.parseDouble(surface));
    }

    @Bindable
    public String getNumberOfRooms() {
        if(property.getNumberOfRooms() == 0) return "";
        return String.valueOf(property.getNumberOfRooms());
    }

    public void setNumberOfRooms(String numberOfRooms) {
        if(numberOfRooms.isEmpty()) {
            property.setNumberOfRooms(0);
            return;
        }
        property.setNumberOfRooms(Integer.parseInt(numberOfRooms));
    }

    @Bindable
    public String getDescription() {
        return property.getDescription();
    }

    public void setDescription(String description) {
        property.setDescription(description);
    }


    @Bindable
    public String getFormattedAddress() {
        return property.getAddress().getFormattedAddress();
    }

    public void setFormattedAddress(String address) {
        property.getAddress().setFormattedAddress(address);
    }

    @Bindable
    public String getLocality() {
        return property.getAddress().getLocality();
    }

    public void setLocality(String locality) {
        property.getAddress().setLocality(locality);
    }

    @Bindable
    public Boolean getSold() {
        return property.isSold();
    }

    public void setSold(Boolean sold) {
        if(property.isSold() != sold){
            property.setSold(sold);
            notifyPropertyChanged(BR.sold);
        }
    }

    @Bindable
    public String getFormattedPublicationDate() {
        return publicationDate;
    }

    public void setFormattedPublicationDate(String date) {
        this.publicationDate = date;
        notifyPropertyChanged(BR.formattedPublicationDate);
    }

    @Bindable
    public String getFormattedSaleDate() {
        return saleDate;
    }

    public void setFormattedSaleDate(String saleDate) {
        this.saleDate = saleDate;
        notifyPropertyChanged(BR.formattedSaleDate);
    }


    public static String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }
}

