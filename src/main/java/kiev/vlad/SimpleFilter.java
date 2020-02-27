package kiev.vlad;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.*;


public class SimpleFilter {

    static EntityManagerFactory emf;
    static EntityManager em;

    public List<SimpleDish> paramForFiltering (String discountStr, String minPriceStr, String maxPriceStr){

        boolean discount = false;
        int minPrice = 0;
        int maxPrice = 0;

        System.out.println ("--paramForFiltering results:--");
        if(!(discountStr==null)) {
            discount = true;
            System.out.println ("discount = " + discount);
        }
        if(!(minPriceStr==null)&&!(minPriceStr.equals (""))) {
            minPrice = Integer.parseInt (minPriceStr);
            System.out.println ("minPrice = " + minPrice);
        }
        if(!(maxPriceStr==null)&&!(maxPriceStr.equals (""))) {
            maxPrice = Integer.parseInt (maxPriceStr);
            System.out.println ("maxPrice = " + maxPrice);
        }


        try {
            // create connection
            emf = Persistence.createEntityManagerFactory("RestaurantMenu");
            em = emf.createEntityManager();
            try {

                System.out.println ("--Print all dishes --");

                CriteriaBuilder cb = em.getCriteriaBuilder ();

                CriteriaQuery<SimpleDish> q;
                q = cb.createQuery (SimpleDish.class);
                Root<SimpleDish> c = q.from (SimpleDish.class);

                ParameterExpression<Boolean> d = cb.parameter (Boolean.class);
                ParameterExpression<Integer> pMin = cb.parameter (Integer.class);
                ParameterExpression<Integer> pMax = cb.parameter (Integer.class);

                q.select (c);

                /*c.get ("price");
                cb.between ( c.<Integer>get ("price"), pMax,pMax);*/

                if(discount!=false)
                    q.where (cb.equal (c.get ("discount"), d));

                if((maxPrice!=0) && (minPrice!=0)){
                    Path<Integer> ss  = c.get ("price");
                    q.where (cb.between (ss, pMin, pMax));
                }
                /*if (maxPrice!=0)
                    q.where (cb.lessThan (c.get ("price"), pMax));
                if (minPrice!=0)
                    q.where (cb.greaterThan (c.<Comparable>get ("price"), pMin));*/


                TypedQuery<SimpleDish> query = em.createQuery (q);
                /**
                 * подставляем параметры
                 */
                if(discount!=false) query.setParameter (d , discount);

                if(maxPrice!=0 && minPrice!=0){
                    query.setParameter (pMin, minPrice);
                    query.setParameter (pMax, maxPrice);
                }

                List<SimpleDish> list = query.getResultList ();

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

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            // create connection
            emf = Persistence.createEntityManagerFactory("RestaurantMenu");
            em = emf.createEntityManager();
            try {
                while (true) {
                    System.out.println("1: add dish");
                    System.out.println("2: add random dish");
                    System.out.println("3: delete dish");
                    System.out.println("4: change dish");
                    System.out.println("5: view dish");
                    System.out.println("6: view view dishs with discount");
                    System.out.println("7: view view dishs with Min-Max price");
                    System.out.println("8: view view dishs with Min-Max price and with discount");
                    System.out.println("9: view view dishs where summ mass less than 1 Kg");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addDish(sc);
                            break;
                        case "2":
                            insertRandomDishs(sc);
                            break;
                        case "3":
                            deleteDish(sc);
                            break;
                        case "4":
                            changeDish(sc);
                            break;
                        case "5":
                            viewDishs2();
                            break;
                        case "6":
                            viewDishsWithDiscount2();
                            break;
                        case "7":
                            viewDishsWithMinMaxPrice (sc);
                            break;
                        case "8":
                            viewDishsWithMinMaxPriceAndDiscount (sc);
                            break;
                        case "9":
                            massLow1Kg ();
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    public static void massLow1Kg (){
        System.out.println ("--Print massLow1Kg --");

        CriteriaBuilder cb = em.getCriteriaBuilder ();

        CriteriaQuery<SimpleDish> q;
        q = cb.createQuery (SimpleDish.class);
        Root<SimpleDish> c = q.from (SimpleDish.class);

        q.select (c);
        q.where (cb.lessThan (c.<Integer>get ("weight"), 1000));
        q.orderBy (cb.asc (c.<Integer>get ("weight")));

        TypedQuery<SimpleDish> query = em.createQuery (q);
        List<SimpleDish> list = query.getResultList ();

        System.out.println ("--from DB--");
        for (SimpleDish dish : list)
            System.out.println(dish);

        System.out.println ("--result--");
        List<SimpleDish> resList = new ArrayList<>();
        double maxWeight=0;
        for (int i = 0; i < list.size(); i++) {
            if(i==0) {
                maxWeight = list.get(i).getWeight();
                resList.add(list.get(i));
                continue;
            }
            if((maxWeight+list.get(i).getWeight())<=1000) {
                maxWeight += list.get(i).getWeight();
                resList.add(list.get(i));
            }
        }

        for (SimpleDish dish : resList)
            System.out.println(dish);


    }

    private static void viewDishsWithMinMaxPriceAndDiscount (Scanner sc) {
        System.out.println ("--Print all dishes WITH Min Max price and with discount--");

        System.out.print("Enter Y if you want to filter with discount: ");
        String sAnswer = sc.nextLine();
        boolean Answer;
        Answer = sAnswer.equals ("Y");
        boolean discount = false;
        if (Answer) discount = true;

        System.out.print("Enter Y if you want to filter WITH Min Max price: ");
        String sAnswer2 = sc.nextLine();
        boolean Answer2;
        Answer2 = sAnswer2.equals ("Y");
        int min = 0;
        int max = 0;
        if(Answer2){
            System.out.print("Enter Dish -  min price: ");
            String sMin = sc.nextLine();
            min = Integer.parseInt (sMin);
            System.out.print("Enter Dish -  max price: ");
            String sMax = sc.nextLine();
            max = Integer.parseInt (sMax);
        }

        CriteriaBuilder cb = em.getCriteriaBuilder ();
        CriteriaQuery<SimpleDish> q;
        q = cb.createQuery (SimpleDish.class);
        Root <SimpleDish> c = q.from (SimpleDish.class);

        ParameterExpression<Boolean> d = cb.parameter (Boolean.class);
        ParameterExpression<Integer> pMin = cb.parameter (Integer.class);
        ParameterExpression<Integer> pMax = cb.parameter (Integer.class);

        ArrayList <Predicate> tasks = new ArrayList<> ();

        if(Answer) {
            tasks.add ( cb.equal (c.get ("discount"), d) );
        }
        if(Answer2) tasks.add ( cb.between (c.<Integer>get ("price"), pMin, pMax) );

        Predicate[] resultTasks = null;



        q.select (c);

        System.out.println ("tasks.size () ="+tasks.size ());
        if(tasks.size ()!=0) {
            resultTasks = tasks.toArray (new  Predicate[tasks.size ()]);
            q.where ( resultTasks);
        }

        TypedQuery<SimpleDish> query = em.createQuery (q);

        if(Answer) query.setParameter (d , discount);;
        if(Answer2) {
            query.setParameter (pMin, min);
            query.setParameter (pMax, max);
        }

        List<SimpleDish> list = query.getResultList ();

        for (SimpleDish dish : list)
            System.out.println(dish);

    }

    private static void viewDishsWithMinMaxPrice (Scanner sc) {
        System.out.println ("--Print all dishes WITH Min Max price--");

        System.out.print("Enter Dish -  min price: ");
        String sMin = sc.nextLine();
        int min = Integer.parseInt (sMin);
        System.out.print("Enter Dish -  max price: ");
        String sMax = sc.nextLine();
        int max = Integer.parseInt (sMax);

        CriteriaBuilder cb = em.getCriteriaBuilder ();
        CriteriaQuery<SimpleDish> q;
        q = cb.createQuery (SimpleDish.class);
        Root <SimpleDish> c = q.from (SimpleDish.class);
        ParameterExpression<Integer> pMin = cb.parameter (Integer.class);
        ParameterExpression<Integer> pMax = cb.parameter (Integer.class);

        Path<Integer> ss  = c.get ("price");
        q.select (c).where (cb.between (ss, pMin, pMax));

        TypedQuery<SimpleDish> query = em.createQuery (q);
        query.setParameter (pMin, min);
        query.setParameter (pMax, max);


        List<SimpleDish> list = query.getResultList ();

        for (SimpleDish dish : list)
            System.out.println(dish);

    }

    private static void addDish(Scanner sc) {
        System.out.print("Enter Dish name: ");
        String name = sc.nextLine();

        em.getTransaction().begin();
        try {
            SimpleDish c = new SimpleDish (name,randomPrice (),randomWeight (),randomDiscount ());
            em.persist(c);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    private static void deleteDish(Scanner sc) {
        System.out.print("Enter Dish id: ");
        String sId = sc.nextLine();
        long id = Long.parseLong(sId);

        SimpleDish c = em.getReference(SimpleDish.class, id);
        if (c == null) {
            System.out.println("Dish not found!");
            return;
        }

        em.getTransaction().begin();
        try {
            em.remove(c);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    private static void changeDish(Scanner sc) {
        System.out.print("Enter Dish name: ");
        String name = sc.nextLine();

        System.out.print("Enter new price: ");
        String sPrice = sc.nextLine();
        int price = Integer.parseInt(sPrice);

        SimpleDish c = null;
        try {
            Query query = em.createQuery(
                    "SELECT c FROM SimpleDish c WHERE c.name = :name", SimpleDish.class);
            query.setParameter("name", name);
            c = (SimpleDish) query.getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("Dish not found!");
            return;
        } catch (NonUniqueResultException ex) {
            System.out.println("Non unique result!");
            return;
        }

        em.getTransaction().begin();
        try {
            c.setPrice (price);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    private static void insertRandomDishs(Scanner sc) {
        System.out.print("Enter Dishs count: ");
        String sCount = sc.nextLine();
        int count = Integer.parseInt(sCount);

        em.getTransaction().begin();
        try {
            HashSet<String> stringsNames = new HashSet<> ();
            boolean add;
            for (int i = 0; i < count; i++) {

                String rndName = randomName ();
                add = stringsNames.add (rndName);
                if (add) {
                    SimpleDish c = new SimpleDish (rndName,randomPrice (),randomWeight (),randomDiscount ());
                    em.persist(c);
                } //else count++;

            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    private static void viewDishs2 () {
        System.out.println ("--Print all dishes --");

        CriteriaBuilder cb = em.getCriteriaBuilder ();

        CriteriaQuery<SimpleDish> q;
        q = cb.createQuery (SimpleDish.class);
        Root<SimpleDish> c = q.from (SimpleDish.class);
        q.select (c);

        TypedQuery<SimpleDish> query = em.createQuery (q);
        List<SimpleDish> list = query.getResultList ();

        /**
         * еквивалентный запрос
         */
        /*
        TypedQuery<kiev.vlad.SimpleDish> query1 =
                em.createQuery("SELECT c FROM kiev.vlad.SimpleDish c", kiev.vlad.SimpleDish.class);
        List<kiev.vlad.SimpleDish> results = query1.getResultList();*/

        //where (cb.like(dishRoot.get("discount"), "true")

        for (SimpleDish dish : list)
            System.out.println(dish);

    }

    private static void viewDishsWithDiscount2 () {
        System.out.println ("--Print all dishes WITH Discount--");

        CriteriaBuilder cb = em.getCriteriaBuilder ();

        CriteriaQuery<SimpleDish> q;
        q = cb.createQuery (SimpleDish.class);
        Root <SimpleDish> c = q.from (SimpleDish.class);

        ParameterExpression<Boolean> d = cb.parameter (Boolean.class);
        q.select (c).where (cb.equal (c.get ("discount"), d));

        TypedQuery<SimpleDish> query = em.createQuery (q);
        query.setParameter (d , true);


        List<SimpleDish> list = query.getResultList ();

        /**
         * еквивалентный запрос
         */
        /*TypedQuery<kiev.vlad.SimpleDish> query1 =
                em.createQuery("SELECT c FROM kiev.vlad.SimpleDish c WHERE c.discount = :d", kiev.vlad.SimpleDish.class);
        query1.setParameter ("discount", true);
        List<kiev.vlad.SimpleDish> results = query1.getResultList();*/

        //where (cb.like(dishRoot.get("d"), "true")

        for (SimpleDish dish : list)
            System.out.println(dish);

    }

    static final String[] NAMES = {
            "ALL-AMERICAN CHEESEBURGER", "BUx1 BURGER", "SOUTHWESTERN BLACK BEAN BURGER",
            "TRADITIONAL WINGS", "BUFFALO RANCH CHICKEN WRAP", "SOUTHWEST PHILLY",
            "BONELESS WINGS", "STREET TACOS", "GARDEN SALAD WITH CHICKEN",
            "FRENCH FRIES", "CARROTS & CELERY", "CHILI",
            "MOZZARELLA STICKS", "BEER-BATTERED ONION RINGS", "BUFFALO MAC & CHEESE"
    };

    static final boolean[] DISCOUNTS = {true,false};
    static final Random RND = new Random();

    static private int getRndPrs50 (int maxValue){
        int var= RND.nextInt(maxValue);
        if ((var+50)%50==0)
            return var;
        else
            return getRndPrs50 (maxValue);
    }

    /*static int randomPrice() {
        return RND.nextInt(500);
    }
    static int randomWeight() {
        return RND.nextInt(3000);
    }*/
    static int randomPrice() {
        return getRndPrs50 (1000);
    }
    static int randomWeight() {
        return getRndPrs50 (3000);
    }
    static String randomName() {
        return NAMES[RND.nextInt(NAMES.length)];
    }
    static boolean randomDiscount() {
        return DISCOUNTS[RND.nextInt(DISCOUNTS.length)];
    }


}
