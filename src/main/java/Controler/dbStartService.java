package Controler;

import model.User;
import model.Valuta;

import java.util.concurrent.Callable;

public class dbStartService extends BaseStart{


    public Valuta saveValuta(final String name) {
        Callable<Valuta> valutaCallable = new Callable<Valuta>() {
            public Valuta call() throws Exception {
                Valuta valuta = new Valuta (name);
                em.persist(valuta);
                return valuta;
            }
        };

        return performTransaction(valutaCallable);
    }

    public User saveUser(final String login, final String pass) {
        Callable<User> userCallable = new Callable<User>() {
            public User call() throws Exception {
                User user = new User(login, pass);
                em.persist(user);
                return user;
            }
        };

        return performTransaction(userCallable);
    }

    public void addAccountToUser (User user1) {
        em.merge (user1);
    }


}