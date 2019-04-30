/**
 * AVC ~ Asistente Virtual para la Comunicación
 *
 * @author: José Miguel Ramírez Sanz
 * @version: 1.0
 */

package com.example.avc.UnitTest;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.avc.R;
import com.example.avc.ResultadoActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;



/**
 * Clase que realiza los test unitarios de la clase ResultadoActivity.
 */
@RunWith(AndroidJUnit4.class)
public class ResultadoActivityTest {

    @Rule
    public ActivityTestRule<ResultadoActivity> mActivityTestRule =
            new ActivityTestRule<ResultadoActivity>(ResultadoActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, ResultadoActivity.class);
                    result.putExtra("paciente", "JMiguel");
                    return result;
                }
            };

    private ResultadoActivity res = null;

    /**
     * Método que se ejecuta al principio de cada test.
     */
    @Before
    public void setUp(){

        res = mActivityTestRule.getActivity();

    }

    /**
     * Test que comprueba que el texto no es nulo.
     */
    @Test
    public void testTextoNoNulo(){
        TextView texto = res.findViewById(R.id.text);
        assertNotNull(texto);
    }

    /**
     * Test que comprueba la visibilidad del ImageButton Grabar.
     */
    @Test
    public void testVisGrb(){
        ImageButton inte = res.findViewById(R.id.bGrb);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    /**
     * Test que comprueba la visibilidad del ImageButton Parar.
     */
    @Test
    public void testVisPar(){
        ImageButton inte = res.findViewById(R.id.bParar);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    /**
     * Test que comprueba la visibilidad del ImageButton Escuchar.
     */
    @Test
    public void testVisEsc(){
        ImageButton inte = res.findViewById(R.id.bRpr);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    /**
     * Test que comprueba la visibilidad del ImageButton Entender.
     */
    @Test
    public void testVisEnt(){
        ImageButton inte = res.findViewById(R.id.bEnte);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    /**
     * Test que comprueba la visibilidad del ImageButton Cancelar.
     */
    @Test
    public void testVisCan(){
        ImageButton inte = res.findViewById(R.id.bCance);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    /**
     * Test que comprueba la visibilidad del ImageButton Información Grabar.
     */
    @Test
    public void testVisInfGrb(){
        ImageButton inte = res.findViewById(R.id.bInfGrb);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    /**
     * Test que comprueba la visibilidad del ImageButton Información Parar.
     */
    @Test
    public void testVisInfPar(){
        ImageButton inte = res.findViewById(R.id.bInfPr);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    /**
     * Test que comprueba la visibilidad del ImageButton Información Escuchar.
     */
    @Test
    public void testVisInfEsc(){
        ImageButton inte = res.findViewById(R.id.bInfRpr);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    /**
     * Test que comprueba que el ImageButton grabar está disponible al comienzo.
     */
    @Test
    public void testEnGrb(){
        ImageButton inte = res.findViewById(R.id.bGrb);

        assertTrue(inte.isEnabled());
    }

    /**
     * Test que comprueba que el ImageButton parar no está disponible al comienzo.
     */
    @Test
    public void testEnPar(){
        ImageButton inte = res.findViewById(R.id.bParar);

        assertFalse(inte.isEnabled());
    }

    /**
     * Test que comprueba que el ImageButton escuchar no está disponible al comienzo.
     */
    @Test
    public void testEnEsc(){
        ImageButton inte = res.findViewById(R.id.bRpr);

        assertFalse(inte.isEnabled());
    }

    /**
     * Test que comprueba que el ImageButton entender no está disponible al comienzo.
     */
    @Test
    public void testEnEnt(){
        ImageButton inte = res.findViewById(R.id.bEnte);

        assertFalse(inte.isEnabled());
    }

    /**
     * Test que comprueba que el ImageButton cancelar está disponible al comienzo.
     */
    @Test
    public void testEnCan(){
        ImageButton inte = res.findViewById(R.id.bCance);

        assertTrue(inte.isEnabled());
    }

    /**
     * Test que comprueba que el ImageButton información grabar está disponible al comienzo.
     */
    @Test
    public void testEnInfGrb(){
        ImageButton inte = res.findViewById(R.id.bInfGrb);

        assertTrue(inte.isEnabled());
    }

    /**
     * Test que comprueba que el ImageButton información parar está disponible al comienzo.
     */
    @Test
    public void testEnInfPar(){
        ImageButton inte = res.findViewById(R.id.bInfPr);

        assertTrue(inte.isEnabled());
    }

    /**
     * Test que comprueba que el ImageButton información escuchar está disponible al comienzo.
     */
    @Test
    public void testEnInfEsc(){
        ImageButton inte = res.findViewById(R.id.bInfRpr);

        assertTrue(inte.isEnabled());
    }

    /**
     * Método que comprueba si al dar el botón grabar se habilita el botón parar.
     */
    @Test
    public void testHabPar(){
        onView(withId(R.id.bGrb)).perform(click());
        ImageButton inte = res.findViewById(R.id.bParar);

        assertTrue(inte.isEnabled());
    }

    /**
     * Método que comprueba que el botón parar sigue habilitado tras intentar parar nada mas empezar a grabar.
     */
    @Test
    public void testGrbPar(){
        onView(withId(R.id.bGrb)).perform(click());
        onView(withId(R.id.bParar)).perform(click());

        ImageButton inte = res.findViewById(R.id.bParar);

        assertTrue(inte.isEnabled());
    }

    /**
     * Método que graba 5 segundos y para la grabación, comprobando que los botones habilitados son correctos.
     * @throws InterruptedException por el Thread.sleep
     */
    @Test
    public void testHaBEE() throws InterruptedException {
        onView(withId(R.id.bGrb)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.bParar)).perform(click());
        ImageButton inte = res.findViewById(R.id.bParar);
        ImageButton inte2 = res.findViewById(R.id.bGrb);
        ImageButton inte3 = res.findViewById(R.id.bRpr);
        ImageButton inte4 = res.findViewById(R.id.bEnte);
        assertFalse(inte.isEnabled());
        assertTrue(inte2.isEnabled());
        assertTrue(inte3.isEnabled());
        assertTrue(inte4.isEnabled());
    }

    /**
     * Método que se ejecuta al final de cada test.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {

        res.finish();

        res = null;

    }
}