package com.chirag.bharaagriassigment.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.chirag.bharaagriassigment.model.entities.Genre;

import java.util.List;

@Dao
public interface GenreDao {
    @Insert
    List<Long> insertAll(Genre... genres);

    @Query("SELECT * FROM genre")
    List<Genre> getAllGenres();

    @Query("SELECT * from genre WHERE genreId = :genreId")
    Genre getGenre(int genreId);

    @Query("DELETE FROM genre")
    void deleteAllGenres();
}
