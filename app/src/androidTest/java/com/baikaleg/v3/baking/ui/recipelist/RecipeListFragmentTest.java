package com.baikaleg.v3.baking.ui.recipelist;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentTransaction;

import com.baikaleg.v3.baking.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeListFragmentTest {
    private static final int ITEM_TO_CLICK = 0;
    private IdlingResource idlingResource;

    @Rule
    public final ActivityTestRule<RecipeListActivity> activityTestRule
            = new ActivityTestRule<>(RecipeListActivity.class);

    @Before
    public void setupFragment() {
        RecipeListFragment recipesFragment =
                (RecipeListFragment) activityTestRule.getActivity()
                        .getSupportFragmentManager().findFragmentById(R.id.fragment);
        FragmentTransaction transaction = activityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, recipesFragment);
        transaction.commit();

        idlingResource = recipesFragment.getIdlingResource();
        Espresso.registerIdlingResources(idlingResource);

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.waitForIdleSync();
    }

    @Test
    public void verifyRecipeDetails() {
        onView(withId(R.id.recipes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(ITEM_TO_CLICK, click()));
        onView(withText(activityTestRule.getActivity().getString(R.string.steps))).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }
}