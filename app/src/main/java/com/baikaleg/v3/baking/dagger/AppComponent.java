package com.baikaleg.v3.baking.dagger;

import android.app.Application;

import com.baikaleg.v3.baking.data.network.RecipeApi;
import com.baikaleg.v3.baking.data.Repository;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent extends AndroidInjector<DaggerApplication> {

    @Override
    void inject(DaggerApplication instance);

    void inject(RecipeApi api);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    Repository getRepository();
} 