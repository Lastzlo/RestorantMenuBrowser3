import model.BankAccount;
import model.User;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserWithBankAccounts extends BaseTest {
    private Long saveTestUserWithBankAccounts(
            String login, String pass, Float amountOfMoney) {

        final User user = new User (login, pass);
        final BankAccount bankAccount1 = new BankAccount (amountOfMoney);
        final BankAccount bankAccount2 = new BankAccount (amountOfMoney+1000);

        user.addBankAccount (bankAccount1);
        user.addBankAccount (bankAccount2);

        return performTransaction (
                new Callable<Long> () {
                    public Long call () throws Exception {
                        em.persist (user);
                        return user.getId ();
                }
        });


    }

    @Test
    public void testPersist() {
        long id = saveTestUserWithBankAccounts("Vsevolod", "1111",
                (float) 1000);

        em.clear();

        User otherUser = em.find (User.class, id);
        assertNotNull (otherUser);

        List <BankAccount> bankAccountList = otherUser.getBankAccounts ();
        assertEquals (2, bankAccountList.size ());

    }

    @Test
    public void testRemoveClient() {
        long id = saveTestUserWithBankAccounts ("Aleksandr", "7777",
                (float) 2000);

        em.clear();

        final User user = em.getReference (User.class, id);
        assertNotNull (user);

        performTransaction (new Callable<Void> () {
            public Void call () throws Exception {
                em.remove (user);
                return null;
            }
        });

    }

    @Test
    public void testRemoveAllBankAccounts() {
        long id = saveTestUserWithBankAccounts ("Anybody", "1234",
                (float) 5050);

        em.clear();

        final User otherUser = em.find (User.class, id);
        assertNotNull (otherUser);

        final List<BankAccount> bankAccountList = otherUser.getBankAccounts ();
        assertEquals (2, bankAccountList.size ());

        performTransaction (new Callable<Void> () {
            public Void call () throws Exception {
                BankAccount bankAccount1 = bankAccountList.get (0);
                BankAccount bankAccount2 = bankAccountList.get (1);

                otherUser.clearBankAccounts ();
                em.merge (otherUser);

                em.remove (bankAccount1);
                em.remove (bankAccount2);

                return null;
            }
        });

        User otherUser2 = em.find (User.class, id);
        assertNotNull (otherUser2);

        List <BankAccount> bankAccountList2 = otherUser2.getBankAccounts ();
        assertEquals (0, bankAccountList2.size ());

    }

    @Test
    public void testRemoveOneBankAccount() {
        long id = saveTestUserWithBankAccounts ("Anybody", "1234",
                (float) 5050);

        em.clear();

        final User otherUser = em.find (User.class, id);
        assertNotNull (otherUser);

        final List<BankAccount> bankAccountList = otherUser.getBankAccounts ();
        assertEquals (2, bankAccountList.size ());

        performTransaction (new Callable<Void> () {
            public Void call () throws Exception {
                BankAccount bankAccount1 = bankAccountList.get (0);
                BankAccount bankAccount2 = bankAccountList.get (1);

                //otherUser.clearBankAccounts ();
                otherUser.removeBankAccount (bankAccount1);
                otherUser.removeBankAccount (bankAccount2);
                em.merge (otherUser);

                em.remove (bankAccount1);
                em.remove (bankAccount2);

                return null;
            }
        });

        User otherUser2 = em.find (User.class, id);
        assertNotNull (otherUser2);

        List <BankAccount> bankAccountList2 = otherUser2.getBankAccounts ();
        assertEquals (0, bankAccountList2.size ());

    }
}
