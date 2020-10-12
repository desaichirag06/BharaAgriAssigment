package com.chirag.bharaagriassigment.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.chirag.bharaagriassigment.model.dao.GenreDao;
import com.chirag.bharaagriassigment.model.dao.MoviesDao;
import com.chirag.bharaagriassigment.model.entities.Genre;
import com.chirag.bharaagriassigment.model.entities.MovieDetail;

@Database(entities = {MovieDetail.class, Genre.class}, version = 1)
@TypeConverters({MoviesDao.IntegerTypeConverter.class})
public abstract class MoviesDatabase extends RoomDatabase {

    private static MoviesDatabase instance;

    public static MoviesDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), MoviesDatabase.class, "MoviesDatabase").build();
        }
        return instance;
    }

    public abstract MoviesDao moviesDao();

    public abstract GenreDao genreDao();
}
