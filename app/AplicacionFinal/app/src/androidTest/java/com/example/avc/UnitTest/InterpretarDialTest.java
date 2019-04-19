/**
 * AVC ~ Asistente Virtual para la Comunicación
 *
 * @author: José Miguel Ramírez Sanz
 * @version: 1.0
 */


package com.example.avc.UnitTest;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.avc.MainActivity;
import com.example.avc.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
/**
 * Clase para los test espresso de los diálogos de la pantalla InterpretarActivity.
 */
public class InterpretarDialTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.RECORD_AUDIO",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    /**
     * Método que comprueba si se despliega el diálogo.
     */
    @Test
    public void testDialEst() {
        ViewInteraction appCompatImageButton = onView(
                allOf(ViewMatchers.withId(R.id.bEntender)));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.bInfoEstado)));
        appCompatImageButton2.perform(click());

        ViewInteraction button = onView(
                allOf(withId(android.R.id.button1)));
        button.check(matches(isDisplayed()));
    }


    /**
     * Método que comprueba si se despliega el diálogo.
     */
    @Test
    public void testDialRes() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bEntender)));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.bInfoPregunta)));
        appCompatImageButton2.perform(click());

        ViewInteraction button = onView(
                allOf(withId(android.R.id.button1)));
        button.check(matches(isDisplayed()));
    }
}
