package com.chirag.bharaagriassigment.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.chirag.bharaagriassigment.model.MoviesDatabase;
import com.chirag.bharaagriassigment.model.entities.MovieDetail;

public class DetailViewModel extends AndroidViewModel {
    public MutableLiveData<MovieDetail> movieLiveData = new MutableLiveData<>();
    private AsyncTask<Integer, Void, MovieDetail> retrieveMoviesTask;

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchMovie(int movieUuid) {
        retrieveMoviesTask = new RetrieveMovieDetailsTask();
        retrieveMoviesTask.execute(movieUuid);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (retrieveMoviesTask != null) {
            retrieveMoviesTask.cancel(true);
            retrieveMoviesTask = null;
        }
    }

    private class RetrieveMovieDetailsTask extends AsyncTask<Integer, Void, MovieDetail> {
        @Override
        protected MovieDetail doInBackground(Integer... integers) {
            int uuid = integers[0];
            return MoviesDatabase.getInstance(getApplication()).moviesDao().getMovie(uuid);
        }

        @Override
        protected void onPostExecute(MovieDetail movieDetail) {
            movieLiveData.setValue(movieDetail);
        }
    }
}
