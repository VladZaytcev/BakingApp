package com.baikaleg.v3.baking.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.baikaleg.v3.baking.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.baking.data.Repository;
import com.baikaleg.v3.baking.data.model.Recipe;
import com.baikaleg.v3.baking.rx.SchedulersFacade;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

@ActivityScoped
public class RecipeListViewModel extends ViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<Recipe>> response = new MutableLiveData<>();

    private final Repository repository;

    private final SchedulersFacade schedulersFacade;

    public RecipeListViewModel(Repository repository, SchedulersFacade schedulersFacade) {
        this.repository = repository;
        this.schedulersFacade = schedulersFacade;

        loadRecipes();
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }

    public MutableLiveData<List<Recipe>> getRecipes() {
        return response;
    }

    private void loadRecipes() {
        disposables.add(repository.getRecipes()
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(response::setValue));
    }
}