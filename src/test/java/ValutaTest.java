
import model.Valuta;
import org.junit.Test;

import java.util.concurrent.Callable;

import static org.junit.Assert.*;

public class ValutaTest extends BaseTest {

    private Valuta saveTestValuta(final String name) {
        Callable<Valuta> valutaCallable = new Callable<Valuta>() {
            public Valuta call() throws Exception {
                Valuta valuta = new Valuta (name);
                em.persist(valuta);
                return valuta;
            }
        };

        return performTransaction(valutaCallable);
    }


    @Test
    public void testPersistAndFind() {
        Valuta valuta = saveTestValuta ("UAH");

        long id = valuta.getId();
        assertTrue(id > 0);
        System.out.println ("id = "+ id);


        // find existing
        Valuta other = em.find (Valuta.class, id);
        assertNotNull (other);
        assertEquals (valuta.getName (), other.getName ());

        // clear context
        em.clear();

        // entity was already loaded by find()
        other = em.getReference(Valuta.class, id);
        assertNotNull(other);
        assertEquals (valuta.getName (), other.getName ());
    }

    /*@Test
    public void testPersistAndFindByName() {
        Valuta valuta = saveTestValuta ("UAH");

        String name = valuta.getName ();
        assertEquals (name, "UAH");

        // find existing
        Valuta other = em.find (Valuta.class, name);


        //Valuta other = em.find (Valuta.class, id);
        assertNotNull (other);
        assertEquals (valuta.getName (), other.getName ());

        // clear context
        em.clear();

        // entity was already loaded by find()
        other = em.getReference(Valuta.class, name);
        assertNotNull(other);
        assertEquals (valuta.getName (), other.getName ());
    }*/

    @Test
    public void testRemove() {
        final Valuta valuta = saveTestValuta ("GRIVNA");
        final long id = valuta.getId();

        performTransaction(new Callable<Void>() {
            public Void call() throws Exception {
                Valuta other = em.getReference(Valuta.class, id);
                em.remove(other);
                return null;
            }
        });

        Valuta another = em.find(Valuta.class, id);
        assertNull(another);
    }


}