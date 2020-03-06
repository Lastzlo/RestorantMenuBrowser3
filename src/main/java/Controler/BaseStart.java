package Controler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.concurrent.Callable;

public abstract class BaseStart {
    private static final String NAME = "startBank";

    private EntityManagerFactory emFactory;
    protected EntityManager em;


    public void init() {
        emFactory = Persistence.createEntityManagerFactory(NAME);
        em = emFactory.createEntityManager();
    }

    public void finish() {
        if (em != null) em.close();
        if (emFactory != null) emFactory.close();
    }

    protected <T> T performTransaction(Callable<T> action) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            T result = action.call();
            transaction.commit();

            return result;
        } catch (Exception ex) {
            if (transaction.isActive())
                transaction.rollback();

            throw new RuntimeException(ex);
        }
    }
}
