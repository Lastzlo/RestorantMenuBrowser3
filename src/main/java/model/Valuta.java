package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "valuts")
public class Valuta {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    /*@OneToMany(mappedBy = "valuta", cascade = CascadeType.ALL)
    private List<BankAccount> bankAccounts = new ArrayList<BankAccount> ();*/
    @OneToMany(cascade = CascadeType.ALL)
    private List<BankAccount> bankAccounts = new ArrayList<BankAccount> ();

    public Valuta (){}

    public Valuta (String name) {
        this.name = name;
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    @Override
    public String toString () {
        return "Valuta{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
