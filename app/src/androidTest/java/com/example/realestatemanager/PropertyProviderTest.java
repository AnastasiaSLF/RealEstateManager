package com.example.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.example.realestatemanager.entities.EstateEntity;
import com.example.realestatemanager.modele.Property;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



@RunWith(AndroidJUnit4.class)
public class PropertyProviderTest {

    private static final EstateEntity fakeProperty =
            new EstateEntity(0,
                    Property.Type.HOUSE,
                    2.5,
                    54.56,
                    5,
                    "desc",
                    new EstateEntity.AddressEntity(),
                    false,
                    464,
                    3254,
                    "");

    private ContentResolver contentResolver;
    private Uri contentUri;

    @Before
    public void setUp() {
        final Context context = ApplicationProvider.getApplicationContext();


        contentResolver = context.getContentResolver();
        contentUri = Uri.parse("content://vnd."
                + context.getString(R.string.property_content_provider_authorities)
                + "/property");
    }

    @Test
    public void should_provide_property_by_id() {
        final Cursor result = contentResolver.query(ContentUris.withAppendedId(contentUri, 1), null, null, null, null);
        Assert.assertEquals(result.getCount(), 1);
    }
}