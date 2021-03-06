package com.baikaleg.v3.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.baikaleg.v3.baking.R;
import com.baikaleg.v3.baking.data.model.Recipe;
import com.baikaleg.v3.baking.ui.recipedetails.RecipeDetailsActivity;
import com.baikaleg.v3.baking.utils.Constants;
import com.google.gson.Gson;

public class RecipeWidgetProvider extends AppWidgetProvider {
    public final static String WIDGET_RECIPE_EXTRA = "widget_recipe_extra";
   private final String TAG = "myLogs";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(TAG, "onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        SharedPreferences sp = context.getSharedPreferences(
                Constants.SP, Context.MODE_PRIVATE);
        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, sp, id);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        SharedPreferences.Editor editor = context.getSharedPreferences(
                Constants.SP, Context.MODE_PRIVATE).edit();
        editor.remove(Constants.RECIPE_PREF);
        editor.apply();
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(TAG, "onDisabled");
    }


    public static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                                    SharedPreferences sp, int appWidgetId) {
        Gson gson = new Gson();
        String jsonRecipe = sp.getString(Constants.RECIPE_PREF, null);
        Recipe recipe = gson.fromJson(jsonRecipe, Recipe.class);

        RemoteViews rv = new RemoteViews(context.getPackageName(),
                R.layout.recipe_widget);
        if (recipe == null) {
            rv.setTextViewText(R.id.recipe_widget_name, context.getString(R.string.app_name));
            return;
        } else {
            rv.setTextViewText(R.id.recipe_widget_name, recipe.getName());
        }

        Intent intent = new Intent(context, RecipeDetailsActivity.class);
        intent.putExtra(RecipeDetailsActivity.EXTRA_RECIPE, recipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        rv.setOnClickPendingIntent(R.id.recipe_widget_name, pendingIntent);

        Intent adapter = new Intent(context, WidgetListService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        adapter.putExtra(WIDGET_RECIPE_EXTRA, jsonRecipe);
        Uri data = Uri.parse(adapter.toUri(Intent.URI_INTENT_SCHEME));
        adapter.setData(data);
        rv.setRemoteAdapter(R.id.recipe_widget_list, adapter);

        appWidgetManager.updateAppWidget(new ComponentName(context, RecipeWidgetProvider.class), rv);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.recipe_widget_list);
    }
}