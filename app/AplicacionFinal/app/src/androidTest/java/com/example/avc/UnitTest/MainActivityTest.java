/**
 * AVC ~ Asistente Virtual para la Comunicación
 *
 * @author: José Miguel Ramírez Sanz
 * @version: 1.0
 */

package com.example.avc.UnitTest;

import android.os.Environment;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

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

import java.io.File;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Clase para la ejecución de los test unitarios de la clase MainActivity.
 */
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private MainActivity main = null;

    private static String ruta,config,rpac;

    private String paciente;

    private Spinner sp;

    /**
     * Método que se ejecuta una vez cuando se crea la clase.
     */
    @BeforeClass
    public static void classSetUp(){
        ruta = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Apace";

        config = ruta +"/config";

        rpac = config + "/paciente.csv";
    }

    /**
     * Método que se ejecuta antes de cada uno de los test de la clase.
     */
    @Before
    public void setUp(){

        main = mActivityTestRule.getActivity();

    }

    /**
     * Test que comprueba si se ha guardado el paciente para la siguiente ejecución ponerlo en el spinner.
     */
    @Test
    public void testPacienteGuardado(){

        paciente = "JMiguel";
        sp = main.findViewById(R.id.spaciente);
        //Para poder acceder al set del spinner
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<String> ad = (ArrayAdapter<String>) sp.getAdapter();
                sp.setSelection(ad.getPosition(paciente));
            }
        });
        main.finish();

        main = mActivityTestRule.getActivity();

        assertEquals(sp.getSelectedItem().toString(),paciente);
    }

    /**
     * Test que comprueba la existencia del directorio raíz de la aplicación.
     */
    @Test
    public void testCreaApace(){

        assertTrue(new File(ruta).exists());
    }

    /**
     * Test que comprueba la existencia del directorio de configuración.
     */
    @Test
    public void testCreaConfig(){

        assertTrue(new File(config).exists());
    }

    /**
     * Test que comprueba la existencia del fichero del paciente.
     */
    @Test
    public void testCreaPaciente(){

        assertTrue(new File(rpac).exists());

    }

    /**
     * Test que comprueba que se crea bien el directorio de configuración tras eliminarlo.
     */
    @Test
    public void testBorrarConfig(){
        File p = new File(config);
        eliminarCarpeta(p);

        main.finish();


        main = mActivityTestRule.getActivity();
        main.inicio();
        assertTrue(new File(config).exists());
    }

    /**
     * Test que comprueba que se crea bien el directorio raíz tras eliminarlo.
     */
    @Test
    public void testBorrarApace(){
        File p = new File(ruta);
        eliminarCarpeta(p);

        main.finish();


        main = mActivityTestRule.getActivity();
        main.inicio();
        assertTrue(new File(ruta).exists());
    }

    /**
     * Test que comrpueba que se crea bien el fichero del paciente tras eliminarlo.
     */
    @Test
    public void testBorrarPaciente(){

        main.finish();

        File p = new File(rpac);
        if(p.isFile()){
            p.delete();
        }

        main = mActivityTestRule.getActivity();

        main.inicio();
        assertTrue(new File(rpac).exists());
    }

    /**
     * Test que comprueba la visibilidad del ImageButton que quiero decir.
     */
    @Test
    public void testVisEnt(){
        ImageButton inte = main.findViewById(R.id.bEntender);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    /**
     * Test que comprueba la visibilidad del ImageButton registro de información.
     */
    @Test
    public void testVisOpc(){
        ImageButton inte = main.findViewById(R.id.bOpc);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    /**
     * Test que comprueba la visibilidad del ImageButton información de que quiero decir.
     */
    @Test
    public void testVisInfEnt(){
        ImageButton inte = main.findViewById(R.id.bInfEnt);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    /**
     * Test que comprueba la visibilidad del ImageButton información de registro de información.
     */
    @Test
    public void testVisInfOpc(){
        ImageButton inte = main.findViewById(R.id.bInfOpc);

        assertEquals(inte.getVisibility(), View.VISIBLE);
    }

    /**
     * Test que comprueba si el ImageButton que quiero decir está habilitado.
     */
    @Test
    public void testEnEnt(){
        ImageButton inte = main.findViewById(R.id.bEntender);

        assertTrue(inte.isEnabled());
    }

    /**
     * Test que comprueba si el ImageButton registro de información está habilitado.
     */
    @Test
    public void testEnOpc(){
        ImageButton inte = main.findViewById(R.id.bOpc);

        assertTrue(inte.isEnabled());
    }

    /**
     * Test que comprueba si el ImageButton información de que quiero decir está habilitado.
     */
    @Test
    public void testEnInfEnt(){
        ImageButton inte = main.findViewById(R.id.bInfEnt);

        assertTrue(inte.isEnabled());
    }

    /**
     * Test que comprueba si el ImageButton información de registro de información está habilitado.
     */
    @Test
    public void testEnInfOpc(){
        ImageButton inte = main.findViewById(R.id.bInfOpc);

        assertTrue(inte.isEnabled());
    }

    /**
     * Método que se ejecuta al final de cada test.
     */
    @After
    public void tearDown(){
        main.finish();
        main = null;

    }

    /**
     * Método que comprueba que se lanza el diálogo.
     */
    @Test
    public void testDialEnt() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bInfEnt)));
        appCompatImageButton.perform(click());

        ViewInteraction button = onView(
                allOf(withId(android.R.id.button1)));
        button.check(matches(isDisplayed()));
    }

    /**
     * Método que comprueba que se lanza el diálogo.
     */
    @Test
    public void testDialOpc() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bInfOpc)));
        appCompatImageButton.perform(click());

        ViewInteraction button = onView(
                allOf(withId(android.R.id.button1)));
        button.check(matches(isDisplayed()));
    }

    /**
     * Método que borra un idrectorio y lo que cuelga de el.
     * @param dir File del directorio a borrar.
     */
    private void eliminarCarpeta(File dir){
        if(dir.isDirectory()){
            for(File hijo: dir.listFiles()){
                if(hijo.isDirectory()){
                    eliminarCarpeta(hijo);
                }else{
                    hijo.delete();
                }
            }
            dir.delete();
        }
    }
}