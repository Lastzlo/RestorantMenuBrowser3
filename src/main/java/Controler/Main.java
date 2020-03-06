package Controler;

import model.BankAccount;
import model.User;
import model.Valuta;

import java.util.List;

public class Main {
    public static void main (String[] args) {
        //создаем валюты и пользователей
        dbStartService dbStartService = new dbStartService ();
        dbStartService.init ();

        Valuta valuta  = dbStartService.saveValuta ("UAH");
        Valuta valuta2 = dbStartService.saveValuta ("EUR");
        Valuta valuta3 = dbStartService.saveValuta ("USD");

        User user  = dbStartService.saveUser ("Andre", "1111");
        User user2 = dbStartService.saveUser ("Nika", "2222");

        dbStartService.finish ();


        //добавляем пользователю Счет
        dbService dbService = new dbService ();
        dbService.init ();

        User otherUser1 =  dbService.getUser ("Andre", "1111");
        Valuta otherValuta1 = dbService.getValuta ("UAH");//валюта 1 - "UAH"
        Float amountOfMoney1 =  (float) 4000;//cума
        BankAccount bankAccount1 = new BankAccount (amountOfMoney1, otherValuta1);
        otherUser1.addBankAccount (bankAccount1);
        dbService.addAccountToUser(otherUser1);

        User otherUser2 =  dbService.getUser ("Nika", "2222");
        Valuta otherValuta2 = dbService.getValuta ("UAH");//валюта 1 - "UAH"
        Float amountOfMoney2 =  (float) 5000;//cума
        BankAccount bankAccount2 = new BankAccount (amountOfMoney2, otherValuta2);
        otherUser2.addBankAccount (bankAccount2);
        dbService.addAccountToUser(otherUser2);

        dbService.finish ();


        //получаем информацию по пользователю
        dbService dbService1 = new dbService ();
        dbService1.init ();
        User otherUser3 =  dbService1.getUser ("Andre", "1111");
        User otherUser4 =  dbService1.getUser ("Nika", "2222");

        dbService1.finish ();

        //поменять валюту счета
        dbService dbService2 = new dbService ();
        dbService2.init ();

        System.out.println ("Begin .. ");
        System.out.println (otherUser3.getBankAccount (0));
        dbService2.changeValutOfBankAccount (otherUser3.getBankAccount (0),  "USD");
        System.out.println ("After .. ");
        System.out.println (otherUser3.getBankAccount (0));

        dbService2.finish ();

        //перевести со счета на счет
        dbService dbService3 = new dbService ();
        dbService3.init ();

        System.out.println ("Begin .. ");
        System.out.println (otherUser3.getBankAccount (0));
        BankAccount bAccount1 = otherUser3.getBankAccount (0);
        System.out.println (otherUser4.getBankAccount (0));
        BankAccount bAccount2 = otherUser4.getBankAccount (0);

        Float sendMoney = (float)20;
        dbService3.sendMoney(bAccount1,bAccount2,sendMoney);

        System.out.println ("After .. ");
        System.out.println (otherUser3.getBankAccount (0));
        System.out.println (otherUser4.getBankAccount (0));

        dbService3.finish ();

        //получить счет/счета
        dbService dbService4 = new dbService ();
        dbService4.init ();

        dbService4.getBankAccount ((long)6);

        List <BankAccount> bankAccountList = dbService4.getBankAccounts ();
        System.out.println ("bankAccountList ...");
        for (BankAccount b: bankAccountList
             ) {
            System.out.println (b);
        }

        dbService4.finish ();





    }
}
