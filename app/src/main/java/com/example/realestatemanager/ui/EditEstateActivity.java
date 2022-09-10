package com.example.realestatemanager.ui;
import static com.example.realestatemanager.modele.Property.PROPERTY_RELATED_DATE_FORMATTER;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.realestatemanager.R;
import com.example.realestatemanager.adapters.AgentSpinnerAdapter;
import com.example.realestatemanager.adapters.PhotoListAdapter;
import com.example.realestatemanager.databinding.ActivityEditEstateBinding;
import com.example.realestatemanager.databinding.PhotoDescriptionEditorLayoutBinding;
import com.example.realestatemanager.modele.Photo;
import com.example.realestatemanager.modele.Property;
import com.example.realestatemanager.modele.Property.PointOfInterest;
import com.example.realestatemanager.view.PointOfInterestView;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.function.Consumer;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class EditEstateActivity extends AppCompatActivity {

    private ActivityEditEstateBinding binding;
    private EditEstateViewModel viewModel;
    private PhotoListAdapter photoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_estate);
        viewModel = new ViewModelProvider(this).get(EditEstateViewModel.class);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setEditMode();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.saveAction) {
            saveProperty();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setEditMode() {
        final int propertyId = getIntent().getIntExtra(EstateDetailActivity.PROPERTY_ID_ARG_KEY, 0);
        if (propertyId != 0) {
            viewModel.updateProperty(propertyId).observe(this, this::configureViews);
            getSupportActionBar().setTitle(R.string.update_property_txt);
        } else {
            viewModel.createNewProperty().observe(this, this::configureViews);
            getSupportActionBar().setTitle(R.string.create_property_txt);
        }
    }

    private void configureViews(EstateDataBinding propertyDataBinding) {
        binding.setProperty(propertyDataBinding);
        configureTypeSelector();
        configureAgentSelector();
        configurePhotoList();
        showPointOfInterests();

        binding.setOnPointOfInterestAdded(this::createPointOfInterest);
        binding.pickPhoto.setOnClickListener(__ -> pickAnImage());
        binding.publicationDate.setOnClickListener(
                __ -> showDatePicker(propertyDataBinding::setFormattedPublicationDate));
        binding.saleDate.setOnClickListener(
                __ -> showDatePicker(propertyDataBinding::setFormattedSaleDate));
    }

    private void configureTypeSelector() {
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Property.Type.names());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.typeSpinner.setAdapter(adapter);
    }

    private void configureAgentSelector() {
        viewModel
                .getAllAgents()
                .observe(
                        this,
                        agentList -> {
                            final AgentSpinnerAdapter adapter = new AgentSpinnerAdapter(this, agentList);
                            binding.agentSelector.setAdapter(adapter);
                        });
    }

    private void configurePhotoList() {
        photoListAdapter = new PhotoListAdapter(viewModel.getPropertyPhotos());
        binding.photoRecyclerView.setAdapter(photoListAdapter);
        binding.photoRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void showPointOfInterests() {
        viewModel
                .getAllPointsOfInterests()
                .observe(
                        this,
                        pointOrInterestList -> {
                            TextView pointOfInterestView;
                            binding.pointOfInterestsContainer.removeViews(
                                    3, binding.pointOfInterestsContainer.getFlexItemCount() - 3);
                            boolean isSelected;
                            for (final PointOfInterest pointOfInterest : pointOrInterestList) {
                                isSelected = viewModel.containsPointOfInterest(pointOfInterest);
                                pointOfInterestView = new PointOfInterestView(this, isSelected);
                                pointOfInterestView.setOnClickListener(this::modifyPointOfInterestState);
                                pointOfInterestView.setText(pointOfInterest.getName());
                                pointOfInterestView.setTag(pointOfInterest);
                                binding.pointOfInterestsContainer.addView(pointOfInterestView);
                            }
                        });
    }

    private void showDatePicker(Consumer<String> onDateSelected) {
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        this,
                        (view, year, month, dayOfMonth) -> {
                            final String formattedDate =
                                    LocalDate.of(year, month, dayOfMonth).format(PROPERTY_RELATED_DATE_FORMATTER);
                            onDateSelected.accept(formattedDate);
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void saveProperty() {
        if (viewModel.isPhotoDefined()) {
            if (viewModel.isSoldFieldValid()) {
                viewModel.persist();
                Snackbar.make(
                        binding.getRoot(),
                        getString(R.string.property_successfully_saved_msg),
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                Snackbar.make(
                        binding.getRoot(), getString(R.string.sold_field_invalid), Snackbar.LENGTH_SHORT)
                        .show();
            }
        } else {
            Snackbar.make(
                    binding.getRoot(), getString(R.string.photo_required_msg), Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    private void createPointOfInterest(String pointOfInterestName) {
        Log.d("CREATE_POINT_OF_I", "CALLED WITH VALUE : " + pointOfInterestName);
        if (pointOfInterestName.length() < 3) return;
        final PointOfInterest pointOfInterest = new PointOfInterest(pointOfInterestName);
        viewModel
                .createPointOfInterest(pointOfInterest)
                .observe(
                        this,
                        pointOfInterestId -> {
                            Log.d("ON_POI_CREATED", "CALLED WITH VALUE : " + pointOfInterestName);
                            if (pointOfInterestId == null || pointOfInterestId.equals(0)) return;
                            final TextView pointOfInterestView = new PointOfInterestView(this);
                            pointOfInterest.setId(pointOfInterestId);
                            pointOfInterestView.setText(pointOfInterestName);
                            pointOfInterestView.setTag(pointOfInterest);
                            binding.addPointOfInterest.setVisibility(View.GONE);
                        });
    }

    private void modifyPointOfInterestState(View pointOfInterestView) {
        ((PointOfInterestView) pointOfInterestView).toggleChecked();
        final PointOfInterest pointOfInterest = (PointOfInterest) pointOfInterestView.getTag();
        if (viewModel.containsPointOfInterest(pointOfInterest)) {
            viewModel.removePointOrInterestFromCurrentProperty(pointOfInterest);
        } else {
            viewModel.addPointOfInterestToCurrentProperty(pointOfInterest);
        }
    }

    private void pickAnImage() {
        ImagePicker.with(this).crop().saveDir(getExternalFilesDir(Environment.DIRECTORY_DCIM)).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                final Photo photo = new Photo();
                photo.setUrl(data.getData().toString());
                showPhotoDescriptionEditor(photo);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Snackbar.make(binding.getRoot(), ImagePicker.getError(data), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void showPhotoDescriptionEditor(Photo photo) {
        final PhotoDescriptionEditorLayoutBinding photoDescLayout =
                PhotoDescriptionEditorLayoutBinding.inflate(getLayoutInflater(), binding.root, false);

        Glide.with(photoDescLayout.getRoot())
                .load(photo.getUrl())
                .centerCrop()
                .into(photoDescLayout.photo);

        new AlertDialog.Builder(this)
                .setTitle(R.string.photo_desc_dialog_title)
                .setView(photoDescLayout.getRoot())
                .setPositiveButton(
                        R.string.set_txt,
                        ((dialog, which) -> {
                            photo.setDescription(photoDescLayout.photoDescription.getText().toString());
                            if (photoDescLayout.mainCheckBox.isChecked()) {
                                viewModel.setMainPhoto(photo);
                            }
                            viewModel.addPhotoToCurrentProperty(photo);
                            photoListAdapter.updateList(viewModel.getPropertyPhotos());
                        }))
                .create()
                .show();
    }
}