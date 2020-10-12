package com.chirag.bharaagriassigment.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverter;

import com.chirag.bharaagriassigment.model.entities.MovieDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface MoviesDao {
    @Insert
    List<Long> insertAll(MovieDetail... movies);

    @Query("SELECT * FROM moviedetail")
    List<MovieDetail> getAllMovies();

    @Query("SELECT * from moviedetail WHERE uuid = :movieId")
    MovieDetail getMovie(int movieId);

    @Query("DELETE FROM moviedetail")
    void deleteAllMovies();

    class IntegerTypeConverter {
        @TypeConverter
        public static ArrayList<Integer> integerToItems(String json) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Integer>>() {
            }.getType();
            return gson.fromJson(json, type);
        }

        @TypeConverter
        public static String itemListToInteger(ArrayList<Integer> list) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Integer>>() {
            }.getType();
            return gson.toJson(list, type);
        }
    }
}
