/**
 * AVC ~ Asistente Virtual para la Comunicación
 *
 * @author: José Miguel Ramírez Sanz
 * @version: 1.0
 */

package com.example.avc.UnitTest;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.avc.InterpretarActivity;
import com.example.avc.MainActivity;
import com.example.avc.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

/**
 * Clase para realizar los test unitarios de la clase InterpretarActivity.
 */
public class InterpretarActivityTest {

    @Rule
    public ActivityTestRule<InterpretarActivity> mActivityTestRule = new ActivityTestRule<>(InterpretarActivity.class);

    private InterpretarActivity inter = null;

    /**
     * Método que se ejecuta al comienzo de cads uno de los test de esta clase.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        inter = mActivityTestRule.getActivity();

    }

    /**
     * Test que comprueba que el texto no es nulo.
     */
    @Test
    public void testTextoNoNulo(){
        TextView texto = inter.findViewById(R.id.texto);
        assertNotNull(texto);
    }

    /**
     * Test que comrpueba la visibilidad del ImageButton que me ocurre o  que me pasa.
     */
    @Test
    public void testVisEst(){
        ImageButton inte = inter.findViewById(R.id.bEstado);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    /**
     * Test que comrpueba la visibilidad del ImageButton responder sí o no.
     */
    @Test
    public void testVisRes(){
        ImageButton inte = inter.findViewById(R.id.bPregunta);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    /**
     * Test que comrpueba la visibilidad del ImageButton información sobre que me ocurre o  que me pasa.
     */
    @Test
    public void testVisInfEst(){
        ImageButton inte = inter.findViewById(R.id.bInfoEstado);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    /**
     * Test que comrpueba la visibilidad del ImageButton información sobre responder sí o no.
     */
    @Test
    public void testVisInfRes(){
        ImageButton inte = inter.findViewById(R.id.bInfoPregunta);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    /**
     * Test que comprueba si el ImageButton que me ocurre o que me pasa está habilitado.
     */
    @Test
    public void testEnEst(){
        ImageButton inte = inter.findViewById(R.id.bEstado);

        assertTrue(inte.isEnabled());
    }

    /**
     * Test que comprueba si el ImageButton responder sí o no está habilitado.
     */
    @Test
    public void testEnRes(){
        ImageButton inte = inter.findViewById(R.id.bPregunta);

        assertTrue(inte.isEnabled());
    }

    /**
     * Test que comprueba si el ImageButton información que me ocurre o que me pasa está habilitado.
     */
    @Test
    public void testEnInfEst(){
        ImageButton inte = inter.findViewById(R.id.bInfoEstado);

        assertTrue(inte.isEnabled());
    }

    /**
     * Test que comprueba si el ImageButton información responder sí o no está habilitado.
     */
    @Test
    public void testEnInfRes(){
        ImageButton inte = inter.findViewById(R.id.bInfoPregunta);

        assertTrue(inte.isEnabled());
    }

    /**
     * Método que se ejecuta al final de cada uno de los test de esta clase.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {

        inter.finish();

        inter = null;

    }
}