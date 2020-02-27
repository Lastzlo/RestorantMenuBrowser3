package kiev.vlad.controler;

import kiev.vlad.SimpleDish;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class DishService {

    static EntityManagerFactory emf;
    static EntityManager em;

    static List<SimpleDish> filter (String discountStr, String minPriceStr, String maxPriceStr, String massLow1KgStr){



        boolean discount = false;
        int minPrice = 0;
        int maxPrice = 0;
        boolean massLow1Kg = false;

        System.out.println ("--paramForFiltering results:--");

        /**
         * проверяем параметры которые пришли в метод
         */
        if(!(discountStr==null)) {
            discount = true;
            System.out.println ("discount = " + discount);
        }
        if(!(minPriceStr==null)) {
            minPrice = Integer.parseInt (minPriceStr);
            System.out.println ("minPrice = " + minPrice);
        }
        if(!(maxPriceStr==null)) {
            maxPrice = Integer.parseInt (maxPriceStr);
            System.out.println ("maxPrice = " + maxPrice);
        }
        if(!(massLow1KgStr==null)) {
            massLow1Kg = true;
            System.out.println ("massLow1Kg = " + massLow1Kg);
        }


        try {
            // create connection
            emf = Persistence.createEntityManagerFactory("RestaurantMenu");
            em = emf.createEntityManager();
            try {

                CriteriaBuilder cb = em.getCriteriaBuilder ();

                CriteriaQuery<SimpleDish> q;
                q = cb.createQuery (SimpleDish.class);
                Root<SimpleDish> c = q.from (SimpleDish.class);

                ParameterExpression<Boolean> pDiscount = cb.parameter (Boolean.class);
                ParameterExpression<Integer> pMin = cb.parameter (Integer.class);
                ParameterExpression<Integer> pMax = cb.parameter (Integer.class);
                ParameterExpression<Boolean> pMassLow1Kg = cb.parameter (Boolean.class);

                /**
                 * Массив заданий которые нужно сделать с БД
                 */
                ArrayList<Predicate> tasks = new ArrayList<> ();

                q.select (c);


                /**
                 * критерии по параметрам которые пришли в метод
                 */
                if(discount)
                    tasks.add ( cb.equal (c.get ("discount"), pDiscount));
                if(maxPrice!=0)
                    tasks.add ( cb.lessThan (c.<Integer>get ("price"), pMax));
                if(minPrice!=0)
                    tasks.add ( cb.greaterThan (c.<Integer>get ("price"), pMin));

                if(massLow1Kg){
                    q.orderBy (cb.asc (c.<Integer>get ("weight")));
                }



                /**
                 * если размер массива 0 то не нужно ничего искать
                 */
                System.out.println ("tasks.size () ="+tasks.size ());
                Predicate[] resultTasks = null;
                if(tasks.size ()!=0) {
                    resultTasks = tasks.toArray (new  Predicate[tasks.size ()]);
                    q.where ( resultTasks);
                }

                TypedQuery<SimpleDish> query = em.createQuery (q);

                /**
                 * устанавливаем параметры query по параметрам которые пришли в метод
                 */
                if(discount)
                    query.setParameter (pDiscount , discount);;
                if(maxPrice!=0)
                    query.setParameter (pMax, maxPrice);
                if(minPrice!=0)
                    query.setParameter (pMin, minPrice);

                List<SimpleDish> list = query.getResultList ();

                if(massLow1Kg){
                    System.out.println ("massLow1Kg filtring...");
                    List<SimpleDish> massList = new ArrayList<>();

                    double maxWeight=list.get(0).getWeight();
                    massList.add(list.get(0));

                    for (int i = 1; i < list.size(); i++) {
                        if((maxWeight+list.get(i).getWeight())<=1000) {
                            maxWeight += list.get(i).getWeight();
                            massList.add(list.get(i));
                        }
                    }
                    return massList;

                } else
                    return list;

            } finally {
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }


    }

}
