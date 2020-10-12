package com.chirag.bharaagriassigment.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.chirag.bharaagriassigment.model.MoviesDatabase;
import com.chirag.bharaagriassigment.model.dao.GenreDao;
import com.chirag.bharaagriassigment.model.entities.Genre;
import com.chirag.bharaagriassigment.model.entities.GenreListResponse;
import com.chirag.bharaagriassigment.model.network.MoviesApiService;
import com.chirag.bharaagriassigment.utilities.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class GenreListViewModel extends AndroidViewModel {

    public MutableLiveData<List<Genre>> genres = new MutableLiveData<>();

    private MoviesApiService genreService = new MoviesApiService();
    private CompositeDisposable disposable = new CompositeDisposable();

    private AsyncTask<List<Genre>, Void, List<Genre>> insertTask;
    private AsyncTask<Void, Void, List<Genre>> retrieveGenresTask;

    private SharedPreferencesHelper prefHelper = SharedPreferencesHelper.getInstance(getApplication());
    private long refreshTime = 5 * 60 * 1000 * 1000 * 1000L;

    public GenreListViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(String api_key) {
        checkCacheDuration();

        long updateTime = prefHelper.getUpdateTime();
        long currentTime = System.nanoTime();
        if (updateTime != 0 && currentTime - updateTime < refreshTime)
            fetchFromDatabase();
        else {
            prefHelper.saveDefaultList(false);
            fetchFromRemote(api_key);
        }
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

    public void refreshBypassCache(String api_key) {
        fetchFromRemote(api_key);
    }


    private void fetchFromDatabase() {
        retrieveGenresTask = new RetrieveGenresTask();
        retrieveGenresTask.execute();
    }

    private void fetchFromRemote(String api_key) {
        disposable.add(
                genreService.getGenres(api_key)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<GenreListResponse>() {
                            @Override
                            public void onSuccess(GenreListResponse genres) {
                                insertTask = new InsertGenreTask();
                                insertTask.execute(genres.getGenres());
                                Toast.makeText(getApplication(), "Genres retrieved from endpoint", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }
                        })
        );
    }

    private void genresRetrieved(List<Genre> genresList) {
        genres.setValue(genresList);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
        if (insertTask != null) {
            insertTask.cancel(true);
            insertTask = null;
        }
        if (retrieveGenresTask != null) {
            retrieveGenresTask.cancel(true);
            retrieveGenresTask = null;
        }
    }

    private class InsertGenreTask extends AsyncTask<List<Genre>, Void, List<Genre>> {

        @Override
        protected List<Genre> doInBackground(List<Genre>... lists) {
            List<Genre> list = lists[0];

            GenreDao dao = MoviesDatabase.getInstance(getApplication()).genreDao();
            dao.deleteAllGenres();

            ArrayList<Genre> newList = new ArrayList<>(list);
            List<Long> result = dao.insertAll(newList.toArray(new Genre[0]));

            int i = 0;
            while (i < list.size()) {
                list.get(i).genreId = result.get(i).intValue();
                ++i;
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<Genre> genres) {
            genresRetrieved(genres);
        }
    }

    private class RetrieveGenresTask extends AsyncTask<Void, Void, List<Genre>> {

        @Override
        protected List<Genre> doInBackground(Void... voids) {
            return MoviesDatabase.getInstance(getApplication()).genreDao().getAllGenres();
        }

        @Override
        protected void onPostExecute(List<Genre> genres) {
            genresRetrieved(genres);
            Toast.makeText(getApplication(), "Genres retrieved from database", Toast.LENGTH_SHORT).show();

        }
    }

}
