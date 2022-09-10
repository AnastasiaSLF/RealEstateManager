package com.example.realestatemanager.fragments;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;


import com.example.realestatemanager.MainActivity;
import com.example.realestatemanager.R;
import com.example.realestatemanager.ViewModel.EstateListViewModel;
import com.example.realestatemanager.adapters.EstateListAdapter;
import com.example.realestatemanager.dialog.EstateFilterDialog;
import com.example.realestatemanager.modele.Property;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EstateListFragment extends Fragment {

    EstateListViewModel viewModel;
    EstateFilterDialog estateFilterDialog;
    EstateListAdapter adapter;
    List<Property> propertyList;
    List<Property.PointOfInterest> pointOfInterestList;

    public static EstateListFragment newInstance() {
        return new EstateListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter) {
            if (estateFilterDialog == null) {
                estateFilterDialog =
                        new EstateFilterDialog(propertyList, pointOfInterestList, adapter::updateList);
            }
            if (!estateFilterDialog.isAdded()) {
                estateFilterDialog.show(getParentFragmentManager(), "filter_dialog");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView =
                (RecyclerView) inflater.inflate(R.layout.estate_list, container, false);
        viewModel = new ViewModelProvider(this).get(EstateListViewModel.class);
        configurePropertyList(recyclerView);
        viewModel
                .getAllPointOfInterest()
                .observe(
                        getViewLifecycleOwner(), pointOfInterests -> pointOfInterestList = pointOfInterests);
        return recyclerView;
    }

    private void configurePropertyList(RecyclerView recyclerView) {
        final MainActivity parentActivity = (MainActivity) getActivity();
        adapter = new EstateListAdapter(new ArrayList<>(), parentActivity::onPropertySelected);
        recyclerView.setAdapter(adapter);
        viewModel
                .getProperties()
                .observe(
                        getViewLifecycleOwner(),
                        properties -> {
                            propertyList = properties;
                            adapter.updateList(properties);
                        });
    }


    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // TODO Refactoring (use bundle args for the dialog dependencies instead of dismissing it on
        //  configuration change)
        if (estateFilterDialog != null && estateFilterDialog.isAdded()) {
            estateFilterDialog.dismiss();
        }
    }

}
