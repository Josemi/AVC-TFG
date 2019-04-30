/**
 * AVC ~ Asistente Virtual para la Comunicación
 *
 * @author: José Miguel Ramírez Sanz
 * @version: 1.0
 */

//Package
package com.example.avc.UnitTest;

//Imports
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.ImageButton;

import com.example.avc.InfoActivity;
import com.example.avc.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Clase que nos permite realizar los test unitarios de la pantalla de información del proyecto.
 */
public class InfoActivityTest {

    @Rule
    public ActivityTestRule<InfoActivity> mActivityTestRule = new ActivityTestRule<>(InfoActivity.class);

    private InfoActivity info=null;

    /**
     * Método que se ejecuta antes de cada uno de los test de la clase.
     */
    @Before
    public void setUp(){

        info = mActivityTestRule.getActivity();

    }

    /**
     * Método que comprueba si el ImageButton aceptar está visible.
     */
    @Test
    public void testVisAcp(){
        ImageButton inte = info.findViewById(R.id.binfoacep);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    /**
     * Método que comprueba si el ImageButton aceptar está disponible.
     */
    @Test
    public void testEnAcp(){
        ImageButton inte = info.findViewById(R.id.binfoacep);

        assertTrue(inte.isEnabled());
    }

    /**
     * Método que se ejecuta al final de cada test.
     */
    @After
    public void tearDown(){
        info.finish();
        info = null;
    }

}