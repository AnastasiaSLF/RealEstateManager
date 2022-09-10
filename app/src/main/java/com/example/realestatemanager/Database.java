package com.example.realestatemanager;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.realestatemanager.dao.EstateDao;
import com.example.realestatemanager.dao.PhotoDao;
import com.example.realestatemanager.dao.PointOfInterestDao;
import com.example.realestatemanager.dao.PropertyPointOfInterestCrossRefDao;
import com.example.realestatemanager.dao.RealEstateAgentDao;
import com.example.realestatemanager.entities.EstateEntity;
import com.example.realestatemanager.entities.PhotoEntity;
import com.example.realestatemanager.entities.PointOfInterestEntity;
import com.example.realestatemanager.entities.RealEstateAgentEntity;
import com.example.realestatemanager.entities.Relationships;
import com.example.realestatemanager.modele.Property;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.Executors;

@androidx.room.Database(
        version = 1,
        entities = {
                EstateEntity.class,
                PhotoEntity.class,
                RealEstateAgentEntity.class,
                PointOfInterestEntity.class,
                Relationships.PropertyPointOfInterestCrossRef.class
        }

)


public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract EstateDao getPropertyDao();

    public abstract RealEstateAgentDao getRealEstateAgentDao();

    public abstract PhotoDao getPhotoDao();

    public abstract PointOfInterestDao getPointOfInterestDao();

    public abstract PropertyPointOfInterestCrossRefDao getPropertyPointOfInterestCrossRefDao();

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            synchronized (Database.class) {
                instance =
                        Room.databaseBuilder(context, Database.class, "real_estate_manager.db")
                                .addCallback(prepopulate)
                                .fallbackToDestructiveMigration()
                                .build();
            }
        }
        return instance;
    }

    public static Database getTestInstance(Context context) {
        return Room.inMemoryDatabaseBuilder(context, Database.class).build();
    }

    // INSERT DEMO DATA

    private static final Callback prepopulate =
            new Callback() {
                @Override
                public void onCreate(@NotNull SupportSQLiteDatabase db) {
                    super.onCreate(db);

                    Executors.newSingleThreadExecutor()
                            .execute(
                                    () -> {
                                        // --------- ---------- AGENT ----------- ---------- //
                                        instance
                                                .getRealEstateAgentDao()
                                                .create(
                                                        new RealEstateAgentEntity(
                                                                "Laurine Mayer"),
                                                        new RealEstateAgentEntity(
                                                                "Leila Rahy"),
                                                        new RealEstateAgentEntity(
                                                                "Samuel Lejardinier"),
                                                        new RealEstateAgentEntity(
                                                                "Rebecca Orain"));

                                        // --------- ---------- PROPERTIES ----------- ---------- //
                                        final int numberOfProperty = 14;

                                        final EstateEntity property1 = getPropertyFixturesModel();
                                        property1.agentID = 2;
                                        property1.setType(Property.Type.HOUSE);

                                        final EstateEntity property2 = getPropertyFixturesModel();
                                        property2.agentID = 4;
                                        property2.setType(Property.Type.APARTMENT);
                                        property2.setSurface(100);
                                        property2.setPrice(6_000_000);
                                        property2.address.setLocality("Manhattan");
                                        property2.address.setFormattedAddress("1515 Park Avenue, NY");
                                        property2.setSold(true);
                                        property2.setPublicationDate(LocalDate.now().minusMonths(4).toEpochDay());
                                        property2.setSaleDate(LocalDate.now().minusMonths(2).toEpochDay());
                                        property2.setMainPhotoUrl(
                                                "https://www.worldelse.com/wp-content/uploads/2021/07/Rennes-copie-scaled.jpg");

                                        final EstateEntity property3 = getPropertyFixturesModel();
                                        property3.agentID = 3;
                                        property3.setPrice(42_000_000);
                                        property3.setSurface(120);
                                        property3.setType(Property.Type.PENTHOUSE);
                                        property3.address.setFormattedAddress("17202 73rd Avenue, NY");
                                        property3.setMainPhotoUrl(
                                                "https://i.insider.com/62839eb74c7379001965152e?width=1000&format=jpeg&auto=webp");

                                        final EstateEntity property4 = getPropertyFixturesModel();
                                        property4.agentID = 4;
                                        property4.setType(Property.Type.HOUSE);
                                        property4.setSurface(45);
                                        property4.address.setLocality("New York");
                                        property4.address.setFormattedAddress("73-22 171st St, Queens, NY");
                                        property4.setPrice(11_000_000);
                                        property4.setMainPhotoUrl(
                                                "https://upload.wikimedia.org/wikipedia/commons/b/b0/Lille_maison_renard.JPG");

                                        final EstateEntity property5 = getPropertyFixturesModel();
                                        property5.agentID = 4;
                                        property5.setPrice(9_000_000);
                                        property5.setSurface(33);
                                        property5.address.setLocality("New York");
                                        property5.address.setFormattedAddress("692 3rd Avenue, NY 10017");
                                        property5.setSold(true);
                                        property5.setPublicationDate(LocalDate.now().minusMonths(10).toEpochDay());
                                        property5.setSaleDate(LocalDate.now().minusMonths(1).toEpochDay());
                                        property5.setMainPhotoUrl(
                                                "https://www.ladresse-paris-tolbiac.com/office7/ladresseAc3-513/catalog/images/pr_p/1/0/2/1/5/4/3/2/10215432a.jpg");

                                        final EstateEntity property6 = getPropertyFixturesModel();
                                        property6.agentID = 1;
                                        property6.setType(Property.Type.HOUSE);
                                        property6.setPrice(21_000_000);
                                        property6.setSurface(66);
                                        property6.address.setLocality("Brooklyn");
                                        property6.address.setFormattedAddress("456 JEFFERSON AVE, NY 11221-1529");
                                        property6.setPublicationDate(LocalDate.now().minusWeeks(1).toEpochDay());
                                        property6.setSaleDate(LocalDate.now().minusMonths(3).toEpochDay());
                                        property6.setSold(true);
                                        property6.setMainPhotoUrl(
                                                "https://www.easyspaces.fr/site/images/normal/OZ.C87.jpg");

                                        final EstateEntity property7 = getPropertyFixturesModel();
                                        property7.agentID = 3;
                                        property7.setPrice(33_000_000);
                                        property7.setSurface(200);
                                        property7.setType(Property.Type.MANOR);
                                        property7.address.setFormattedAddress("42-16 Vernon Boulevard, NY");
                                        property7.setMainPhotoUrl(
                                                "https://cf.bstatic.com/xdata/images/hotel/max500/6085930.jpg?k=93547765c8c930a65b06b7ddfeb1cbaab936cd7db7401c8554c836a1525989ec&o=&hp=1");

                                        instance
                                                .getPropertyDao()
                                                .create(
                                                        property1, property2, property6, property3, property4, property5,
                                                        property7);

                                        property1.agentID = 3;
                                        property2.agentID = 1;
                                        property3.agentID = 2;
                                        property4.agentID = 4;
                                        property5.agentID = 1;
                                        property6.agentID = 2;
                                        property7.agentID = 3;

                                        instance
                                                .getPropertyDao()
                                                .create(
                                                        property1, property2, property6, property3, property4, property5,
                                                        property7);

                                        final PhotoDao photoDao = instance.getPhotoDao();
                                        for (int propertyId = 1; propertyId <= numberOfProperty; propertyId++) {
                                            photoDao.create(
                                                    new PhotoEntity(
                                                            "https://www.vivons-maison.com/sites/default/files/styles/740px/public/field/image/residence-de-grand-standing-ville.jpg?itok=5-75SChA",
                                                            "\n" +
                                                                    "Living room", propertyId),
                                                    new PhotoEntity(
                                                            "https://www.interiorzine.com/wp-content/uploads/2014/12/symbolic-inner-courtyard-skylights.jpg",
                                                            "Staircase",
                                                            propertyId),
                                                    new PhotoEntity(
                                                            "https://i0.wp.com/lilm.co/wp-content/uploads/2021/10/cuisine-haut-de-gamme-pierre-et-blanc.jpeg?fit=1600%2C1068&ssl=1",
                                                            "Kitchen",
                                                            propertyId),
                                                    new PhotoEntity(
                                                            "https://www.stylesdebain.fr/wp-content/uploads/2018/10/Envie-de-salle-de-bain-Style-Factory.jpg",
                                                            "Bathroom",
                                                            propertyId),
                                                    new PhotoEntity(
                                                            "https://images.unsplash.com/photo-1613545325278-f24b0cae1224?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTJ8fGhvbWUlMjBpbnRlcmlvcnxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&w=600&q=80",
                                                            "Living room", propertyId),
                                                    new PhotoEntity(
                                                            "https://i.pinimg.com/550x/fc/96/8f/fc968f834b3a7309642e21da97fdb0e5.jpg",
                                                            "Library",
                                                            propertyId),
                                                    new PhotoEntity(
                                                            "https://www.designferia.com/sites/default/files/images/jardin-amenagement-outdoor-moderne-piscine.jpg",
                                                            "Swimming Pool",
                                                            propertyId),
                                                    new PhotoEntity(
                                                            "https://static.cotemaison.fr/medias_11852/w_420,c_limit,g_north/v1528201567/agence-morvant-moingeon_6068666.jpg",
                                                            "Garden",
                                                            propertyId));
                                        }

                                        // ------------- ------------ POINT_OF_INTEREST -------------- ------------//
                                        final int numberOfPointOfInterest = 5;
                                        instance
                                                .getPointOfInterestDao()
                                                .create(
                                                        new PointOfInterestEntity("School"),
                                                        new PointOfInterestEntity("Restaurant"),
                                                        new PointOfInterestEntity("Hotel"),
                                                        new PointOfInterestEntity("Grocery"),
                                                        new PointOfInterestEntity("Hospital"));

                                        // ------------- POINT_OF_INTEREST - PROPERTY ASSOCIATION -----------------//
                                        final PropertyPointOfInterestCrossRefDao propertyPOIAssocDao =
                                                instance.getPropertyPointOfInterestCrossRefDao();
                                        int numberOfPOIByProperty;
                                        Relationships.PropertyPointOfInterestCrossRef propertyPOIAssocTable =
                                                new Relationships.PropertyPointOfInterestCrossRef();
                                        int currentPointOfInterestId;

                                        for (int propertyId = 1; propertyId <= numberOfProperty; propertyId++) {

                                            propertyPOIAssocTable.propertyId = propertyId;
                                            numberOfPOIByProperty = new Random().nextInt(numberOfPointOfInterest) + 1;

                                            while (numberOfPOIByProperty-- > 0) {
                                                currentPointOfInterestId =
                                                        new Random().nextInt(numberOfPointOfInterest) + 1;
                                                propertyPOIAssocTable.pointOfInterestId = currentPointOfInterestId;
                                                propertyPOIAssocDao.create(propertyPOIAssocTable);
                                            }
                                        }
                                    });
                }
            };

    private static EstateEntity getPropertyFixturesModel() {
        return new EstateEntity(
                0,
                Property.Type.APARTMENT,
                18_000_000,
                70,
                6,
                lorem,
                new EstateEntity.AddressEntity(
                        "New York", "POS TAL", "694 Park Avenue"),
                false,
                LocalDate.now().toEpochDay(),
                0,
                "https://thumbor.forbes.com/thumbor/fit-in/1200x0/filters%3Aformat%28jpg%29/https%3A%2F%2Fspecials-images.forbesimg.com%2Fimageserve%2F1026205392%2F0x0.jpg");
    }

    private static final String lorem =
            "Au coeur du centre ville, au calme, et sans vis à vis , " +
                    "charmante maison de 145m2 comprenant une belle pièce de vie," +
                    " cuisine aménagée et équipée, 4 chambres dont une au RDC et une suite parentale " +
                    "avec dressing et salle d'eau, salle de bains, dépendance," +
                    " l'ensemble sur un magnifique jardin paysager clos de murs";
}


