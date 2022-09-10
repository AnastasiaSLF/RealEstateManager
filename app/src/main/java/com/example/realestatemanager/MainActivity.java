package com.example.realestatemanager;

import static com.example.realestatemanager.ui.EstateDetailActivity.LAYOUT_ORIENTATION_KEY;
import static com.example.realestatemanager.ui.EstateDetailActivity.PROPERTY_ID_ARG_KEY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.realestatemanager.fragments.EstateDetailFragment;
import com.example.realestatemanager.fragments.EstateListFragment;
import com.example.realestatemanager.modele.Property;
import com.example.realestatemanager.ui.EditEstateActivity;
import com.example.realestatemanager.ui.EstateDetailActivity;
import com.example.realestatemanager.ui.LoanSimulatorActivity;
import com.example.realestatemanager.ui.MapActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    EstateDetailFragment propertyDetailFragment;
    private boolean isLargeScreen;
    private int selectedPropertyID;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        findViewById(R.id.createProperty)
                .setOnClickListener(__ -> startActivity(new Intent(this, EditEstateActivity.class)));
        isLargeScreen = findViewById(R.id.detailFragmentContainer) != null;
        Log.d("MAIN_ACTIVITY", "__ isLargeScreen = " + isLargeScreen);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.editProperty) {
            startEditActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViews() {
        setupListFragment();
        if (isLargeScreen) {
            setupDetailFragment();
        }
        setupNavigationDrawer();

    }
    private void setupNavigationDrawer(){
        final DrawerLayout drawerLayout = findViewById(R.id.root);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        final NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
    }
    private boolean onNavigationItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.map) {
            startActivity(new Intent(this, MapActivity.class));
            return true;
        } else if(menuItem.getItemId() == R.id.loanSimulator){
            startActivity(new Intent(this, LoanSimulatorActivity.class));
        }
        return false;
    }

    private void setupListFragment() {
        final EstateListFragment propertyListFragment = EstateListFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.listFragmentContainer, propertyListFragment)
                .commit();
    }

    private void setupDetailFragment() {
        final Bundle args = new Bundle();
        args.putInt(LAYOUT_ORIENTATION_KEY, LinearLayout.HORIZONTAL);
        propertyDetailFragment = EstateDetailFragment.newInstance(args);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.detailFragmentContainer, propertyDetailFragment)
                .commit();
    }

    public void onPropertySelected(Property property) {
        if (isLargeScreen) {
            propertyDetailFragment.setProperty(property);
            selectedPropertyID = property.getId();
        } else {
            startDetailActivity(property.getId());
        }
    }
    private void startDetailActivity(int propertyId) {
        final Bundle arguments = new Bundle();
        arguments.putInt(PROPERTY_ID_ARG_KEY, propertyId);
        arguments.putInt(LAYOUT_ORIENTATION_KEY, LinearLayout.VERTICAL);
        final Intent startDetailIntent = new Intent(this, EstateDetailActivity.class);
        startDetailIntent.putExtras(arguments);
        startActivity(startDetailIntent);
    }

    private void startEditActivity() {
        if(selectedPropertyID == 0){
            showSnackBar("No property have been selected yet");
            return;
        }
        final Bundle args = new Bundle();
        args.putInt(PROPERTY_ID_ARG_KEY, selectedPropertyID);
        final Intent editPropertyIntent = new Intent(this, EditEstateActivity.class);
        editPropertyIntent.putExtras(args);
        startActivity(editPropertyIntent);
    }

    private void showSnackBar(String message){
        final View root = findViewById(R.id.root);
        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show();
    }
}
