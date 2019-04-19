/**
 * AVC ~ Asistente Virtual para la Comunicación
 *
 * @author: José Miguel Ramírez Sanz
 * @version: 1.0
 */

package com.example.avc.UnitTest;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.avc.InterpretarActivity;
import com.example.avc.R;
import com.example.avc.RFinalActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Clase que realiza los test unitario de la clase RFinalActivity.
 */
public class RFinalActivityTest {

    private static String ruta,config;

    //Pongo esto para poder pasar por parámetro el audio para que no de error al crear el File.
    @Rule
    public ActivityTestRule<RFinalActivity> mActivityTestRule =
            new ActivityTestRule<RFinalActivity>(RFinalActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, RFinalActivity.class);
                    result.putExtra("audio", config);
                    return result;
                }
            };

    private RFinalActivity res = null;

    /**
     * Método que se ejecuta al crear la clase.
     */
    @BeforeClass
    public static void classSetUp(){

        ruta = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Apace";

        config = ruta +"/config";

    }

    /**
     * Método que se ejecuta antes de cada test.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {



        res = mActivityTestRule.getActivity();

    }

    /**
     * Test que comprueba que el texto no es nulo.
     */
    @Test
    public void testTextoNoNulo(){
        TextView texto = res.findViewById(R.id.ttexto);
        assertNotNull(texto);
    }

    /**
     * Test que comprueba la visibilidad del ImageButton Aceptar.
     */
    @Test
    public void testVisAcp(){
        ImageButton inte = res.findViewById(R.id.bAcept);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    @Test
    public void testVisEsc(){
        ImageButton inte = res.findViewById(R.id.brepro);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    @Test
    public void testEnAcp(){
        ImageButton inte = res.findViewById(R.id.bAcept);

        assertTrue(inte.isEnabled());
    }

    @Test
    public void testEnEsc(){
        ImageButton inte = res.findViewById(R.id.brepro);

        assertTrue(inte.isEnabled());
    }

    @Test
    public void testResultado(){
        ImageView i1 = res.findViewById(R.id.im1);
        ImageView i2 = res.findViewById(R.id.im2);
        ImageView i3 = res.findViewById(R.id.im3);
        ImageView i4 = res.findViewById(R.id.im4);
        ImageView i5 = res.findViewById(R.id.im5);

        TextView r1 = res.findViewById(R.id.r1);
        TextView r2 = res.findViewById(R.id.r2);
        TextView r3 = res.findViewById(R.id.r3);
        TextView r4 = res.findViewById(R.id.r4);
        TextView r5 = res.findViewById(R.id.r5);

        assertEquals(i1.getVisibility(), View.VISIBLE);
        assertEquals(i2.getVisibility(), View.VISIBLE);
        assertEquals(r1.getVisibility(), View.VISIBLE);
        assertEquals(r2.getVisibility(), View.VISIBLE);

        assertEquals(i3.getVisibility(), View.INVISIBLE);
        assertEquals(i4.getVisibility(), View.INVISIBLE);
        assertEquals(i5.getVisibility(), View.INVISIBLE);

        assertEquals(r3.getVisibility(), View.INVISIBLE);
        assertEquals(r4.getVisibility(), View.INVISIBLE);
        assertEquals(r5.getVisibility(), View.INVISIBLE);
    }



    @After
    public void tearDown() throws Exception {

        res.finish();

        res = null;

    }
}