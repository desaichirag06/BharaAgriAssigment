package com.chirag.bharaagriassigment.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.chirag.bharaagriassigment.model.MoviesDatabase;
import com.chirag.bharaagriassigment.model.entities.Genre;

public class GenreViewModel extends AndroidViewModel {
    public MutableLiveData<Genre> genreLiveData = new MutableLiveData<>();
    private AsyncTask<Integer, Void, Genre> retrieveGenresTask;

    public GenreViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchGenre(int genreUuid) {
        retrieveGenresTask = new RetrieveGenresTask();
        retrieveGenresTask.execute(genreUuid);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (retrieveGenresTask != null) {
            retrieveGenresTask.cancel(true);
            retrieveGenresTask = null;
        }
    }

    private class RetrieveGenresTask extends AsyncTask<Integer, Void, Genre> {
        @Override
        protected Genre doInBackground(Integer... integers) {
            int uuid = integers[0];
            return MoviesDatabase.getInstance(getApplication()).genreDao().getGenre(uuid);
        }

        @Override
        protected void onPostExecute(Genre movieDetail) {
            genreLiveData.setValue(movieDetail);
        }
    }
}
