package com.chirag.bharaagriassigment.view.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chirag.bharaagriassigment.BuildConfig;
import com.chirag.bharaagriassigment.R;
import com.chirag.bharaagriassigment.databinding.FragmentListBinding;
import com.chirag.bharaagriassigment.utilities.SharedPreferencesHelper;
import com.chirag.bharaagriassigment.view.MoviesListAdapter;
import com.chirag.bharaagriassigment.viewmodel.GenreListViewModel;
import com.chirag.bharaagriassigment.viewmodel.MoviesListViewModel;

import java.util.ArrayList;

import static com.chirag.bharaagriassigment.utilities.Constants.LIST_TYPE_POPULAR;
import static com.chirag.bharaagriassigment.utilities.Constants.LIST_TYPE_TOP_RATED;

public class ListFragment extends Fragment {

    FragmentListBinding mBinding;
    Menu myMenu;
    private MoviesListViewModel moviesListViewModel;
    private GenreListViewModel genreListViewModel;
    private MoviesListAdapter moviesListAdapter = new MoviesListAdapter(new ArrayList<>(), new ArrayList<>());
    private SharedPreferencesHelper prefHelper;

    public ListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        setHasOptionsMenu(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        moviesListViewModel = new ViewModelProvider(this).get(MoviesListViewModel.class);
        genreListViewModel = new ViewModelProvider(this).get(GenreListViewModel.class);
        moviesListViewModel.refresh(BuildConfig.THE_MOVIEDB_API_KEY, LIST_TYPE_POPULAR);
        genreListViewModel.refresh(BuildConfig.THE_MOVIEDB_API_KEY);

        mBinding.rvMoviesList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mBinding.rvMoviesList.setAdapter(moviesListAdapter);

        prefHelper = SharedPreferencesHelper.getInstance(getContext());

        mBinding.refreshLayout.setOnRefreshListener(() -> {
            mBinding.rvMoviesList.setVisibility(View.GONE);
            mBinding.tvListError.setVisibility(View.GONE);
            mBinding.loadingView.setVisibility(View.VISIBLE);
            moviesListViewModel.refreshBypassCache(BuildConfig.THE_MOVIEDB_API_KEY, LIST_TYPE_POPULAR);
            genreListViewModel.refreshBypassCache(BuildConfig.THE_MOVIEDB_API_KEY);
            mBinding.refreshLayout.setRefreshing(false);
        });
        observeViewModel();
    }

    private void observeViewModel() {
        genreListViewModel.genres.observe(getViewLifecycleOwner(), genres -> {
            if (genres != null) {
                moviesListAdapter.updateGenreList(genres);
            }
        });

        moviesListViewModel.movies.observe(getViewLifecycleOwner(), movies -> {
            if (movies != null) {
                mBinding.rvMoviesList.setVisibility(View.VISIBLE);
                moviesListAdapter.updateMoviesList(movies);
            }
        });
        moviesListViewModel.movieLoadError.observe(getViewLifecycleOwner(), isError -> {
            if (isError != null) {
                mBinding.tvListError.setVisibility(isError ? View.VISIBLE : View.GONE);
            }
        });

        moviesListViewModel.loading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                mBinding.loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading) {
                    mBinding.tvListError.setVisibility(View.GONE);
                    mBinding.rvMoviesList.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_menu, menu);
        myMenu = menu;
        if (!prefHelper.getDefaultList()) {
            setMenuItemVisibility(R.id.actionPopular, false, R.id.actionTopRated, true);
        } else {
            setMenuItemVisibility(R.id.actionPopular, true, R.id.actionTopRated, false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSettings:
                if (isAdded()) {
                    Navigation.findNavController(getView()).navigate(ListFragmentDirections.actionSettings());
                }
                break;

            case R.id.actionPopular:
                if (isAdded()) {
                    moviesListViewModel.refreshBypassCache(BuildConfig.THE_MOVIEDB_API_KEY, LIST_TYPE_POPULAR);
                    if (myMenu != null) {
                        prefHelper.saveDefaultList(false);
                        setMenuItemVisibility(R.id.actionTopRated, true, R.id.actionPopular, false);
                    }
                }
                break;

            case R.id.actionTopRated:
                if (isAdded()) {
                    moviesListViewModel.refreshBypassCache(BuildConfig.THE_MOVIEDB_API_KEY, LIST_TYPE_TOP_RATED);
                    if (myMenu != null) {
                        prefHelper.saveDefaultList(true);
                        setMenuItemVisibility(R.id.actionPopular, true, R.id.actionTopRated, false);
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMenuItemVisibility(int p, boolean b, int p2, boolean b2) {
        myMenu.findItem(p).setVisible(b);
        myMenu.findItem(p).setEnabled(b);
        myMenu.findItem(p2).setVisible(b2);
        myMenu.findItem(p2).setEnabled(b2);
    }

}