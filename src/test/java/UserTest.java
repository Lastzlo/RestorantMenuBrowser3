import model.User;
import org.junit.Test;

import java.util.concurrent.Callable;

import static org.junit.Assert.*;

public class UserTest extends BaseTest {

    private User saveTestUser(final String login, final String pass) {
        Callable<User> userCallable = new Callable<User>() {
            public User call() throws Exception {
                User user = new User (login, pass);
                em.persist(user);
                return user;
            }
        };

        return performTransaction(userCallable);
    }

    @Test
    public void testPersistAndFind() {
        User user = saveTestUser ("Nikolay", "1234");

        long id = user.getId();
        assertTrue(id > 0);

        // find existing
        User other = em.find(User.class, id);
        assertNotNull(other);
        assertEquals(user.getLogin (), other.getLogin ());
        assertEquals(user.getPass (), other.getPass ());

        // clear context
        em.clear();

        // entity was already loaded by find()
        other = em.getReference(User.class, id);
        assertNotNull(other);
        assertEquals(user.getLogin (), other.getLogin ());
        assertEquals(user.getPass (), other.getPass ());
    }

    @Test(expected = RuntimeException.class)
    public void testNullable() {
        saveTestUser ("Nikolay", null);
    }

    @Test
    public void testMerge() {
        final User client = saveTestUser ("Ivan", "1111");
        long id = client.getId();

        performTransaction(new Callable<Void>() {
            public Void call() throws Exception {
                client.setPass ("2222");

                return null;
            }
        });

        em.clear();

        User other = em.find(User.class, id);
        assertEquals("2222", other.getPass ());
    }

    @Test
    public void testRemove() {
        final User user = saveTestUser ("Ivan", "4321");
        final long id = user.getId();

        performTransaction(new Callable<Void>() {
            public Void call() throws Exception {
                User other = em.getReference(User.class, id);
                em.remove(other);
                return null;
            }
        });

        User another = em.find(User.class, id);
        assertNull(another);
    }


}
