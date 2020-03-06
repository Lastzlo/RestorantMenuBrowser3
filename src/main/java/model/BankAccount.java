package model;

import javax.persistence.*;


@Entity
@Table(name = "bankAccounts")
public class BankAccount {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

/*
    @ManyToOne
    @JoinColumn(name = "valuta")
    private Valuta valuta;
*/

    @ManyToOne
    private Valuta valuta;

    private Float amountOfMoney;



    public BankAccount () {}

    public BankAccount (Float amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }



    public BankAccount (Float amountOfMoney, Valuta valuta) {
        this.valuta = valuta;
        this.amountOfMoney = amountOfMoney;
    }

    public Valuta getValuta () {
        return valuta;
    }

    public void setValuta (Valuta valuta) {
        this.valuta = valuta;
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public User getUser () {
        return user;
    }

    public void setUser (User user) {
        this.user = user;
    }

    public Float getAmountOfMoney () {
        return amountOfMoney;
    }

    public void setAmountOfMoney (Float amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }


    @Override
    public String toString () {
        return "BankAccount{" +
                "id=" + id +
                ", valuta=" + valuta +
                ", amountOfMoney=" + amountOfMoney +
                '}';
    }


}
