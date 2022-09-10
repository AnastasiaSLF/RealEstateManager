package com.example.realestatemanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.realestatemanager.R;
import com.example.realestatemanager.fragments.EstateDetailFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EstateDetailActivity extends AppCompatActivity {

    public static final String PROPERTY_ID_ARG_KEY = "PROPERTY_ID_ARG_KEY";
    public static final String LAYOUT_ORIENTATION_KEY = "LAYOUT_ORIENTATION_KEY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estate_detail);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        showFragment();
    }

    private void showFragment() {
        final Bundle args = getIntent().getExtras();
        args.putInt(LAYOUT_ORIENTATION_KEY, LinearLayout.VERTICAL);
        final EstateDetailFragment fragment = EstateDetailFragment.newInstance(args);
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.editProperty) {
            final Intent editPropertyIntent = new Intent(this, EditEstateActivity.class);
            editPropertyIntent.putExtras(getIntent().getExtras());
            startActivity(editPropertyIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
