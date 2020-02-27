package kiev.vlad;

import javax.persistence.*;

@Entity
@Table(name="Dish")
public class SimpleDish {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="price", nullable = false)
    int price;

    @Column(name="weight", nullable = false)
    private int weight;

    @Column(name="discount")
    boolean discount;

    public SimpleDish (){}

    public SimpleDish (String name, int price, int weight, boolean discount) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.discount = discount;
    }

    @Override
    public String toString () {
        return "myRestaurant1.Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", weight='" + weight + '\'' +
                ", discount=" + discount +
                '}';
    }

    public void setPrice (int price) {
        this.price = price;
    }

    public long getId () {
        return id;
    }

    public String getName () {
        return name;
    }

    public int getPrice () {
        return price;
    }

    public int getWeight () {
        return weight;
    }

    public boolean isDiscount () {
        return discount;
    }
}
