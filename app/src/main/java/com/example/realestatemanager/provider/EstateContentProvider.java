package com.example.realestatemanager.provider;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.realestatemanager.R;
import com.example.realestatemanager.dao.EstateDao;
import com.example.realestatemanager.di.EstateContentProviderEntry;

import dagger.hilt.android.EntryPointAccessors;

public class EstateContentProvider extends ContentProvider {

    EstateDao propertyDao;

    @Override
    public boolean onCreate() {
        propertyDao =
                EntryPointAccessors.fromApplication(
                        getContext().getApplicationContext(), EstateContentProviderEntry.class)
                        .getDatabase()
                        .getPropertyDao();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(
            @NonNull Uri uri,
            @Nullable String[] projection,
            @Nullable String selection,
            @Nullable String[] selectionArgs,
            @Nullable String sortOrder) {
        if (getContext() != null) {
            final int propertyId = (int) ContentUris.parseId(uri);
            final Cursor result = propertyDao.getAsCursor(propertyId);
            result.setNotificationUri(getContext().getContentResolver(), uri);
            return result;
        }
        throw new IllegalStateException(
                "This method shouldn't be invoked before the PropertyProvider have been completely created");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.item/"
                + "vnd."
                + getContext().getString(R.string.property_content_provider_authorities)
                + ".property";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new IllegalAccessError("This provider do not allow write access");
    }

    @Override
    public int delete(
            @NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new IllegalAccessError("This provider do not allow write access");
    }

    @Override
    public int update(
            @NonNull Uri uri,
            @Nullable ContentValues values,
            @Nullable String selection,
            @Nullable String[] selectionArgs) {
        throw new IllegalAccessError("This provider do not allow write access");
    }
}