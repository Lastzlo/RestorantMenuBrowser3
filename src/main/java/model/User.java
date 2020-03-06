package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String pass;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BankAccount> bankAccounts = new ArrayList<BankAccount> ();

    public User () {}

    public User (String login, String pass){
        this.login = login;
        this.pass  = pass;
    }

    public void addBankAccount(BankAccount bankAccount) {
        if ( ! bankAccounts.contains(bankAccount)) {
            bankAccounts.add(bankAccount);
            bankAccount.setUser (this);
        }
    }

    public BankAccount getBankAccount(int index) {
        return bankAccounts.get(index);
    }

    public void removeBankAccount(BankAccount bankAccount) {
        bankAccounts.remove (bankAccount);

    }

    public void clearBankAccounts() {
        bankAccounts.clear();
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getLogin () {
        return login;
    }

    public void setLogin (String login) {
        this.login = login;
    }

    public String getPass () {
        return pass;
    }

    public void setPass (String pass) {
        this.pass = pass;
    }

    public List<BankAccount> getBankAccounts () {
        return bankAccounts;
    }

    public void setBankAccounts (List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }



    @Override
    public String toString () {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", pass='" + pass + '\'' +
                ", bankAccounts=" + printBankAccounts() +
                '}';
    }



    private String printBankAccounts (){
        StringBuilder result = new StringBuilder (" ");
        for (BankAccount b: this.bankAccounts
             ) {
            result.append (b);
        }
        return result.toString ();
    }
}
