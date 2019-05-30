/**
 * AVC ~ Asistente Virtual para la Comunicación
 *
 * @author: José Miguel Ramírez Sanz
 * @version: 1.0
 */

package com.example.avc.IntegrationTest;


import android.support.test.espresso.DataInteraction;
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

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
/**
 * Clase para las pruebas de integración con espresso.
 */
public class IntegrationTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.RECORD_AUDIO",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    /**
     * Método que comprueba la navegabilidad hacía la pantalla de selección de la interpretación.
     */
    @Test
    public void testMovInter() {
        ViewInteraction appCompatImageButton = onView(
                allOf(ViewMatchers.withId(R.id.bEntender)));
        appCompatImageButton.perform(click());

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.bEstado)));
        imageButton.check(matches(isDisplayed()));
    }

    /**
     * Método que comprueba la navegabilidad hacía la pantalla de opciones.
     */
    @Test
    public void testMovOpc() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bOpc)));
        appCompatImageButton.perform(click());

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.bGuardar)));
        imageButton.check(matches(isDisplayed()));
    }

    /**
     * Test para comprobar la selección del paciente y el paso de este a la pantalla de selección de interpretación.
     */
    @Test
    public void testPacCorInter() {
        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spaciente)));
        appCompatSpinner.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(2);
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bEntender)));
        appCompatImageButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.texto)));
        textView.check(matches(withText("Tipo de interpretación para JFrancisco")));
    }

    /**
     * Método que comprueba la navegabilidad hacía atrás de la pantalla opciones.
     */
    @Test
    public void testSalOpc() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bOpc)));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.bC)));
        appCompatImageButton2.perform(click());

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.bEntender)));
        imageButton.check(matches(isDisplayed()));
    }

    /**
     * Método que comprueba que el paciente seleccionado se pasa correctamente a la pantalla de opciones.
     */
    @Test
    public void testPacCorOpc() {
        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spaciente)));
        appCompatSpinner.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(2);
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bOpc)));
        appCompatImageButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.tttexto)));
        textView.check(matches(withText("Opciones almacenadas para JFrancisco")));
    }

    /**
     * Método que comprueba que se pasa correctamente el paciente y el tipo de interpretación estado a Resultado.
     */
    @Test
    public void testEstPac() {
        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spaciente)));
        appCompatSpinner.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(3);
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bEntender)));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.bEstado)));
        appCompatImageButton2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.text)));
        textView.check(matches(withText("Grabe a Cesar para poder interpretar su estado.")));
    }

    /**
     *  Método que comprueba que se pasa correctamente el paciente y el tipo de interpretación respuesta a Resultado.
     */
    @Test
    public void testResPac() {
        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spaciente)));
        appCompatSpinner.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(3);
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bEntender)));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.bPregunta)));
        appCompatImageButton2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.text)));
        textView.check(matches(withText("Grabe a Cesar para poder interpretar su respuesta.")));
    }

    /**
     * Método que comprueba la navegabilidad hacía atrás desde la pantalla de interpretación con el botón salir.
     */
    @Test
    public void testInteSal() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bEntender)));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.bCancelar)));
        appCompatImageButton2.perform(click());

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.bEntender)));
        imageButton.check(matches(isDisplayed()));
    }

    /**
     * Método que comprueba la navegabilidad hacía atrás desde la pantalla de interpretación con el botón atrás del dispositivo.
     */
    @Test
    public void testInteAtr() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bEntender)));
        appCompatImageButton.perform(click());

        pressBack();

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.bEntender)));
        imageButton.check(matches(isDisplayed()));
    }

    /**
     * Método que comprueba la navegabilidad hacía atrás desde la pantalla de resultado con el botón salir.
     */
    @Test
    public void testResSal() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bEntender)));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.bEstado)));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.bCance)));
        appCompatImageButton3.perform(click());

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.bEstado)));
        imageButton.check(matches(isDisplayed()));
    }

    /**
     * Método que comprueba la navegabilidad hacía atrás desde la pantalla de resultado con el botón atrás del dispositivo.
     */
    @Test
    public void testResAtr() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bEntender)));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.bEstado)));
        appCompatImageButton2.perform(click());

        pressBack();

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.bEstado)));
        imageButton.check(matches(isDisplayed()));
    }

    /**
     * Método que comprueba la navegabilidad hacía atrás desde la pantalla de resultado mientras se está grabando con el botón salir.
     */
    @Test
    public void testGrbSal() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bEntender)));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.bEstado)));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.bGrb)));
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withId(R.id.bCance)));
        appCompatImageButton4.perform(click());

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.bEstado)));
        imageButton.check(matches(isDisplayed()));
    }

    /**
     * Método que comprueba la navegabilidad hacía atrás desde la pantalla de resultado mientras se está grabando con el botón atrás del dispositivo.
     */
    @Test
    public void testGrbAtr() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bEntender)));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.bEstado)));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.bGrb)));
        appCompatImageButton3.perform(click());

        pressBack();

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.bEstado)));
        imageButton.check(matches(isDisplayed()));
    }

    /**
     * Método que comprueba el paso del paciente y el tipo de interpretación estado a la pantalla del resultado final.
     */
    @Test
    public void testPacEstRF() {
        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spaciente)));
        appCompatSpinner.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bEntender)));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.bEstado)));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.bGrb)));
        appCompatImageButton3.perform(click());

        try{
            Thread.sleep(2000);
        }catch(InterruptedException ex){
            ex.printStackTrace();
        }

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withId(R.id.bParar)));
        appCompatImageButton4.perform(click());

        ViewInteraction appCompatImageButton5 = onView(
                allOf(withId(R.id.bEnte)));
        appCompatImageButton5.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.ttexto)));
        textView.check(matches(withText("Resultado de la grabación de Sergio sobre su estado.")));
    }

    /**
     * Método que comprueba el paso del paciente y el tipo de interpretación respuesta a la pantalla del resultado final.
     */
    @Test
    public void testPacResRF() {
        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spaciente)));
        appCompatSpinner.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bEntender)));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.bPregunta)));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.bGrb)));
        appCompatImageButton3.perform(click());

        try{
            Thread.sleep(2000);
        }catch(InterruptedException ex){
            ex.printStackTrace();
        }

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withId(R.id.bParar)));
        appCompatImageButton4.perform(click());

        ViewInteraction appCompatImageButton5 = onView(
                allOf(withId(R.id.bEnte)));
        appCompatImageButton5.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.ttexto)));
        textView.check(matches(withText("Resultado de la grabación de Sergio sobre su estado.")));
    }

    /**
     * Comprobar que tras una ejecución correcta con el botón aceptar volvemos a la pantalla inicial.
     */
    @Test
    public void testAcepRF() {
        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spaciente)));
        appCompatSpinner.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bEntender)));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.bEstado)));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.bGrb)));
        appCompatImageButton3.perform(click());

        try{
            Thread.sleep(2000);
        }catch(InterruptedException ex){
            ex.printStackTrace();
        }

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withId(R.id.bParar)));
        appCompatImageButton4.perform(click());

        ViewInteraction appCompatImageButton5 = onView(
                allOf(withId(R.id.bEnte)));
        appCompatImageButton5.perform(click());

        ViewInteraction appCompatImageButton6 = onView(
                allOf(withId(R.id.bAcept)));
        appCompatImageButton6.perform(click());

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.bEntender)));
        imageButton.check(matches(isDisplayed()));
    }

    /**
     * Comprueba que nos hemos movido a la pntalla InfoActivity tras pulsar el ImageButton información de AVC en el menú principal.
     */
    @Test
    public void testMovInfo() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bInfAVC)));
        appCompatImageButton.perform(click());

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.binfoacep)));
        imageButton.check(matches(isDisplayed()));
    }

    /**
     * Test que comrpueba que volvemos al menú principal tras pulsar el botón aceptar en InfoActivity.
     */
    @Test
    public void testInfoAcep() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bInfAVC)));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.binfoacep)));
        appCompatImageButton2.perform(click());

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.bEntender)));
        imageButton.check(matches(isDisplayed()));
    }

    /**
     * Método que comprueba que se vuelve al menú principal si se pulsa el botón atras del dispositivo si estamos en InfoActivity.
     */
    @Test
    public void testInfoAtr() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bInfAVC)));
        appCompatImageButton.perform(click());

        pressBack();

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.bEntender)));
        imageButton.check(matches(isDisplayed()));
    }

    /**
     * Método generado por espresso para la posión usada en la selección del spinner.
     * @param parentMatcher
     * @param position
     * @return
     */
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

}
