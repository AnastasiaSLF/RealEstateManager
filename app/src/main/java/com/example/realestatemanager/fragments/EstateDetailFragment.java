package com.example.realestatemanager.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.realestatemanager.ViewModel.EstateDetailViewModel;
import com.example.realestatemanager.adapters.PhotoListAdapter;
import com.example.realestatemanager.databinding.ActivityDetailViewBinding;
import com.example.realestatemanager.modele.Property;
import com.example.realestatemanager.ui.EstateDetailActivity;
import com.example.realestatemanager.utils.LocationUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EstateDetailFragment extends Fragment {

    EstateDetailViewModel viewModel;
    private int propertyId;
    private int footerLayoutOrientation;
    ActivityDetailViewBinding binding;

    @Inject public LocationUtil locationUtil;

    public static EstateDetailFragment newInstance(Bundle args) {
        EstateDetailFragment fragment = new EstateDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle arguments = requireArguments();
        propertyId = arguments.getInt(EstateDetailActivity.PROPERTY_ID_ARG_KEY);
        footerLayoutOrientation = arguments.getInt(EstateDetailActivity.LAYOUT_ORIENTATION_KEY);
        viewModel = new ViewModelProvider(this).get(EstateDetailViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = ActivityDetailViewBinding.inflate(inflater, container, false);
        binding.setDefaultPointOFInterest(new ArrayList<>());
        binding.setFooterOrientation(footerLayoutOrientation);
        if (propertyId != 0) {
            viewModel.getPropertyById(propertyId).observe(getViewLifecycleOwner(), this::setProperty);
        }
        return binding.getRoot();
    }

    public void setProperty(Property property) {
        binding.setProperty(property);
        binding.photoRecyclerView.setAdapter(new PhotoListAdapter(property.getPhotoList()));


        final String staticMapUrl = locationUtil.getLocationStaticMapFromAddress(property.getAddress().getFormattedAddress());
        Glide.with(binding.getRoot()).load(staticMapUrl).into(binding.locationPreview);
    }
}
