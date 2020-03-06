package Controler;

import model.BankAccount;
import model.User;
import model.Valuta;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.concurrent.Callable;

public class dbService extends BaseWork {


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

    public User getUser(Long id){
        // find existing
        User user = em.find(User.class, id);
        return user;
    }

    public void addAccountToUser (final User user1) {
        //em.merge (user1);

        performTransaction(
            new Callable<Void>() {
                public Void call() throws Exception {
                    em.merge(user1);

                    return null;
                }
            }
        );
    }

    public void getValuta () {

        final long id_valuta = 1;

        TypedQuery<Valuta> countQuery = em.createQuery("SELECT c FROM model.Valuta c WHERE c.id = :id", Valuta.class);
        countQuery.setParameter("id", id_valuta);
        Valuta valuta = countQuery.getSingleResult();

       System.out.println ("valuta.getName () = "+valuta.getName ());
    }

    public Valuta getValuta (Long id_valuta) {
        TypedQuery<Valuta> countQuery = em.createQuery("SELECT c FROM model.Valuta c WHERE c.id = :id", Valuta.class);
        countQuery.setParameter("id", id_valuta);
        Valuta valuta = countQuery.getSingleResult();

        System.out.println ("valuta.getName () = "+valuta.getName ());
        return valuta;
    }

    public BankAccount getBankAccount (Long id_bankAccount) {
        TypedQuery<BankAccount> countQuery = em.createQuery("SELECT c FROM model.BankAccount c WHERE c.id = :id", BankAccount.class);
        countQuery.setParameter("id", id_bankAccount);
        BankAccount bankAccount = countQuery.getSingleResult();

        System.out.println ("bankAccount = "+bankAccount);
        return bankAccount;
    }

    public Valuta getValuta (final String name) {
        TypedQuery<Valuta> countQuery = em.createQuery("SELECT c FROM model.Valuta c WHERE c.name = :name", Valuta.class);
        countQuery.setParameter("name", name);
        Valuta valuta = countQuery.getSingleResult();
        System.out.println ("getValuta (final String name) = "+valuta);
        return valuta;
    }

    public User getUser (){
        final String login ="Nika";
        final String pass ="2222";

        /*TypedQuery<User> countQuery = em.createQuery("SELECT c FROM model.User c WHERE c.login = :login", User.class);*/
        TypedQuery<User> countQuery = em.createQuery("SELECT c FROM model.User c WHERE c.login = :login AND c.pass = :pass", User.class);
        countQuery.setParameter("login", login);
        countQuery.setParameter("pass", pass);
        User user = countQuery.getSingleResult();

        System.out.println ("getUser () = "+user);
        return user;
    }

    public User getUser (final String login, final String pass){
        System.out.println ("getUser --");
        System.out.println ("login = "+login);
        System.out.println ("pass = "+pass);

        TypedQuery<User> countQuery = em.createQuery("SELECT c FROM model.User c WHERE c.login = :login AND c.pass = :pass", User.class);
        countQuery.setParameter("login", login);
        countQuery.setParameter("pass", pass);
        User user = countQuery.getSingleResult();

        System.out.println ("getUser () = "+user);
        return user;
    }

    public void changeValutOfBankAccount (User user) {

        final BankAccount bankAccount = user.getBankAccount (0);
        System.out.println ("user.getBankAccount (0)= " + bankAccount);

        String newValutaStr = "USD";
        Valuta newValuta = getValuta (newValutaStr);
        System.out.println ("New valuta USD = "+ newValuta);

        ValutaService valutaService = new ValutaService ();

        Float toUsd = valutaService.changeValuta (bankAccount.getValuta ().getId (), newValuta.getId (), bankAccount.getAmountOfMoney ());

        bankAccount.setValuta (newValuta);
        bankAccount.setAmountOfMoney (toUsd);

        performTransaction(
                new Callable<Void>() {
                    public Void call() throws Exception {
                        em.merge(bankAccount);

                        return null;
                    }
                }
        );

        System.out.println ("user.getBankAccount (0)= " + bankAccount);


    }

    public void changeValutOfBankAccount (final BankAccount bankAccount, String newValutaStr) {


        System.out.println ("user.getBankAccount (0)= " + bankAccount);

        Valuta newValuta = getValuta (newValutaStr);
        System.out.println ("New valuta USD = "+ newValuta);

        ValutaService valutaService = new ValutaService ();

        Float newAmountOfMoney = valutaService.changeValuta (bankAccount.getValuta ().getId (), newValuta.getId (), bankAccount.getAmountOfMoney ());

        bankAccount.setValuta (newValuta);
        bankAccount.setAmountOfMoney (newAmountOfMoney);

        performTransaction(
                new Callable<Void>() {
                    public Void call() throws Exception {
                        em.merge(bankAccount);

                        return null;
                    }
                }
        );

        System.out.println ("user.getBankAccount (0)= " + bankAccount);


    }

    public void sendMoney (final BankAccount bAccount1, final BankAccount bAccount2) {
        Float sendMoney = (float)20;

        Float startMoney1 = bAccount1.getAmountOfMoney ();
        Valuta startValuta1 = bAccount1.getValuta ();
        Float startMoney2 = bAccount2.getAmountOfMoney ();
        Valuta startValuta2 = bAccount2.getValuta ();

        bAccount1.setAmountOfMoney (startMoney1-sendMoney);

        ValutaService valutaService = new ValutaService ();
        Float newAmountOfMoney = valutaService.changeValuta (startValuta1.getId (), startValuta2.getId (), sendMoney);
        bAccount2.setAmountOfMoney (startMoney2+newAmountOfMoney);

/**
 * commit to db
 */
        performTransaction(
                new Callable<Void>() {
                    public Void call() throws Exception {
                        em.merge(bAccount1);
                        em.merge(bAccount2);

                        return null;
                    }
                }
        );

    }

    public void sendMoney (final BankAccount bAccount1, final BankAccount bAccount2, final Float sendMoney) {
        Float startMoney1 = bAccount1.getAmountOfMoney ();
        Valuta startValuta1 = bAccount1.getValuta ();
        Float startMoney2 = bAccount2.getAmountOfMoney ();
        Valuta startValuta2 = bAccount2.getValuta ();

        bAccount1.setAmountOfMoney (startMoney1-sendMoney);

        ValutaService valutaService = new ValutaService ();
        Float newAmountOfMoney = valutaService.changeValuta (startValuta1.getId (), startValuta2.getId (), sendMoney);
        bAccount2.setAmountOfMoney (startMoney2+newAmountOfMoney);

/**
 * commit to db
 */
        performTransaction(
                new Callable<Void>() {
                    public Void call() throws Exception {
                        em.merge(bAccount1);
                        em.merge(bAccount2);

                        return null;
                    }
                }
        );

    }

    public List<BankAccount> getBankAccounts () {
        TypedQuery<BankAccount> countQuery = em.createQuery("SELECT c FROM model.BankAccount c ", BankAccount.class);
        List<BankAccount> bankAccountList= countQuery.getResultList ();
        return bankAccountList;
    }
}