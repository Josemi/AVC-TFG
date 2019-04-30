/**
 * AVC ~ Asistente Virtual para la Comunicación
 *
 * @author: José Miguel Ramírez Sanz
 * @version: 1.0
 */

package com.example.avc.UnitTest;

import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.avc.InterpretarActivity;
import com.example.avc.OpcionesActivity;
import com.example.avc.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Clase para las pruebas unitarias de la clase OpcionesActivity
 */
public class OpcionesActivityTest {

    @Rule
    public ActivityTestRule<OpcionesActivity> mActivityTestRule = new ActivityTestRule<>(OpcionesActivity.class);

    private OpcionesActivity opc = null;

    /**
     * Método que se ejecuta al comienzo de cada test de la clase.
     */
    @Before
    public void setUp(){

        opc = mActivityTestRule.getActivity();

    }

    /**
     * Comprobar que el texto no es nulo.
     */
    @Test
    public void testTextoNoNulo(){
        TextView texto = opc.findViewById(R.id.tttexto);
        assertNotNull(texto);
    }

    /**
     * Comprobar la visibilidad del ImageButton Guardar.
     */
    @Test
    public void testVisGua(){
        ImageButton inte = opc.findViewById(R.id.bGuardar);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    /**
     * Comprobar la visibilidad del ImageButton Cancelar.
     */
    @Test
    public void testVisCan(){
        ImageButton inte = opc.findViewById(R.id.bC);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    /**
     * Comprobar si el ImageButton Guardar está deshabilitado al comienzo.
     */
    @Test
    public void testEnGua(){
        ImageButton inte = opc.findViewById(R.id.bGuardar);

        assertFalse(inte.isEnabled());
    }

    /**
     * Comprobar si el ImageButton Cancelar está habilitado al comienzo.
     */
    @Test
    public void testEnCan(){
        ImageButton inte = opc.findViewById(R.id.bC);

        assertTrue(inte.isEnabled());
    }

    /**
     * Método que se ejecuta al final de cada test de la clase.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {

        opc.finish();

        opc = null;

    }
}