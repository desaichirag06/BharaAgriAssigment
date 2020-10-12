package com.chirag.bharaagriassigment.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.chirag.bharaagriassigment.model.MoviesDatabase;
import com.chirag.bharaagriassigment.model.dao.MoviesDao;
import com.chirag.bharaagriassigment.model.entities.MovieDetail;
import com.chirag.bharaagriassigment.model.entities.MovieListResponse;
import com.chirag.bharaagriassigment.model.network.MoviesApiService;
import com.chirag.bharaagriassigment.utilities.NotificationsHelper;
import com.chirag.bharaagriassigment.utilities.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MoviesListViewModel extends AndroidViewModel {

    public MutableLiveData<List<MovieDetail>> movies = new MutableLiveData<>();
    public MutableLiveData<Boolean> movieLoadError = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();

    private MoviesApiService moviesService = new MoviesApiService();
    private CompositeDisposable disposable = new CompositeDisposable();

    private AsyncTask<List<MovieDetail>, Void, List<MovieDetail>> insertTask;
    private AsyncTask<Void, Void, List<MovieDetail>> retrieveMoviesTask;

    private SharedPreferencesHelper prefHelper = SharedPreferencesHelper.getInstance(getApplication());
    private long refreshTime = 5 * 60 * 1000 * 1000 * 1000L;

    public MoviesListViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(String api_key, String type) {
        checkCacheDuration();

        long updateTime = prefHelper.getUpdateTime();
        long currentTime = System.nanoTime();
        if (updateTime != 0 && currentTime - updateTime < refreshTime)
            fetchFromDatabase();
        else {
            prefHelper.saveDefaultList(false);
            fetchFromRemote(api_key, type);
        }
    }

    public void refreshBypassCache(String api_key, String type) {
        fetchFromRemote(api_key, type);
    }

    private void checkCacheDuration() {
        String cachePreference = prefHelper.getCacheDuration();

        if (!cachePreference.equals("")) {
            try {
                int cachePreferenceInt = Integer.parseInt(cachePreference);
                refreshTime = cachePreferenceInt * 1000 * 1000 * 1000L;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void fetchFromDatabase() {
        loading.setValue(true);
        retrieveMoviesTask = new RetrieveMoviesTask();
        retrieveMoviesTask.execute();
    }

    private void fetchFromRemote(String api_key, String type) {
        loading.setValue(true);
        disposable.add(
                moviesService.getMovies(type, api_key)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<MovieListResponse>() {
                            @Override
                            public void onSuccess(MovieListResponse movieDetails) {
                                insertTask = new InsertMoviesTask();
                                insertTask.execute(movieDetails.getResults());
                                Toast.makeText(getApplication(), "Movies retrieved from endpoint", Toast.LENGTH_SHORT).show();
                                NotificationsHelper.getInstance(getApplication()).createNotification();
                            }

                            @Override
                            public void onError(Throwable e) {
                                movieLoadError.setValue(true);
                                loading.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }

    private void moviesRetrieved(List<MovieDetail> moviesList) {
        movies.setValue(moviesList);
        movieLoadError.setValue(false);
        loading.setValue(false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
        if (insertTask != null) {
            insertTask.cancel(true);
            insertTask = null;
        }
        if (retrieveMoviesTask != null) {
            retrieveMoviesTask.cancel(true);
            retrieveMoviesTask = null;
        }
    }

    private class InsertMoviesTask extends AsyncTask<List<MovieDetail>, Void, List<MovieDetail>> {

        @Override
        protected List<MovieDetail> doInBackground(List<MovieDetail>... lists) {
            List<MovieDetail> list = lists[0];

            MoviesDao dao = MoviesDatabase.getInstance(getApplication()).moviesDao();
            dao.deleteAllMovies();

            ArrayList<MovieDetail> newList = new ArrayList<>(list);
            List<Long> result = dao.insertAll(newList.toArray(new MovieDetail[0]));

            int i = 0;
            while (i < list.size()) {
                list.get(i).uuid = result.get(i).intValue();
                ++i;
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<MovieDetail> movieDetails) {
            moviesRetrieved(movieDetails);
            prefHelper.saveUpdateTime(System.nanoTime());
        }
    }

    private class RetrieveMoviesTask extends AsyncTask<Void, Void, List<MovieDetail>> {

        @Override
        protected List<MovieDetail> doInBackground(Void... voids) {
            return MoviesDatabase.getInstance(getApplication()).moviesDao().getAllMovies();
        }

        @Override
        protected void onPostExecute(List<MovieDetail> movieDetails) {
            moviesRetrieved(movieDetails);
            Toast.makeText(getApplication(), "Movies retrieved from database", Toast.LENGTH_SHORT).show();

        }
    }

}
