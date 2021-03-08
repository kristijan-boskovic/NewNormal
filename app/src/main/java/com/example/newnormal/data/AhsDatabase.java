package com.example.newnormal.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.newnormal.data.models.Offer;
import com.example.newnormal.data.dao.OfferDao;
import com.example.newnormal.data.models.Reservation;
import com.example.newnormal.data.dao.ReservationDao;
import com.example.newnormal.data.models.User;
import com.example.newnormal.data.dao.UserDao;

import java.util.Random;

@Database(entities = {Offer.class, User.class, Reservation.class}, version = 1, exportSchema = false)
public abstract class AhsDatabase extends RoomDatabase {
    private static AhsDatabase instance;

    public abstract OfferDao offerDao();
    public abstract UserDao userDao();
    public abstract ReservationDao reservationDao();


    public static synchronized AhsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AhsDatabase.class, "ahs_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private OfferDao offerDao;
        private UserDao userDao;

        private PopulateDbAsyncTask(AhsDatabase db) {
            offerDao = db.offerDao();
            userDao = db.userDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Random generator = new Random();

            User user1 = new User(Math.abs(generator.nextInt()), "Kristijan Bošković", "kristijan.boskovic@fer.hr", "kiki123", "Zagreb", 10000, "0958392719");
            User user2 = new User(Math.abs(generator.nextInt()), "Marko Horvat", "marko.horvat@gmail.com", "marko123", "Split", 21000, "0958342613");
            User user3 = new User(Math.abs(generator.nextInt()), "Ivan Jurić", "ivan.juric@gmail.com", "ivan123", "Zagreb", 10000, "0918292919");

            userDao.insert(user1);
            userDao.insert(user2);
            userDao.insert(user3);

            offerDao.insert(new Offer(user1.getUserId(), "Apartman Solaris", "Ilica 15", "Zagreb", "10000", "Dvije sobe, doručak, wifi", "350"));
            offerDao.insert(new Offer(user2.getUserId(), "Hotel Velebit", "Marulićeva ulica 24", "Split", "21000", "Jedna sobe, doručak, wifi", "200"));
            offerDao.insert(new Offer(user1.getUserId(), "Apartman Lens", "Rudeška cesta 10", "Zagreb", "10000", "Dvije sobe, wifi", "150"));
            offerDao.insert(new Offer(user1.getUserId(), "Apartman Primus", "Poljačka ulica 5", "Zagreb", "10000", "Dvije sobe, doručak", "150"));
            offerDao.insert(new Offer(user2.getUserId(), "Hotel Adriatica", "Gundulićeva ulica 63", "Split", "21000", "Dvije sobe, doručak, wifi", "500"));
            return null;
        }
    }
}
