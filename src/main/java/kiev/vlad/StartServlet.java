package kiev.vlad;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;


@WebServlet("/start")
public class StartServlet extends HttpServlet {

    static EntityManagerFactory emf;
    static EntityManager em;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String strNumOfDishes = req.getParameter("numOfDishes");
        int numOfDishes = 10;          //дефолтное количество продуктов

        if(!(strNumOfDishes==null)&&!(strNumOfDishes.equals (""))) {
            numOfDishes = Integer.parseInt (strNumOfDishes);
            System.out.println ("numOfDishes = " + numOfDishes);
        }

        try {
            // create connection
            emf = Persistence.createEntityManagerFactory("CreateMenu");
            em = emf.createEntityManager();
            try {

                System.out.println ("--insertRandomDishes--"); //заполняем данными
                insertRandomDishes(numOfDishes);

                System.out.println ("--Print all dishes --");

                CriteriaBuilder cb = em.getCriteriaBuilder ();

                CriteriaQuery<SimpleDish> q;
                q = cb.createQuery (SimpleDish.class);
                Root<SimpleDish> c = q.from (SimpleDish.class);
                q.select (c);

                TypedQuery<SimpleDish> query = em.createQuery (q);
                List<SimpleDish> list = query.getResultList ();


                /*for (kiev.vlad.SimpleDish dish : list)
                    System.out.println(dish);*/

                req.setAttribute("dishList", list);

                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/menu2.jsp");
                dispatcher.forward(req, resp);


            } finally {
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void insertRandomDishes(int numOfDishes) {

        em.getTransaction().begin();
        try {
            HashSet<String> stringsNames = new HashSet<> ();
            boolean add;
            for (int i = 0; i < numOfDishes; i++) {

                String rndName = randomName ();
                add = stringsNames.add (rndName);
                if (add) {
                    SimpleDish c = new SimpleDish (rndName,randomPrice (),randomWeight (),randomDiscount ());
                    em.persist(c);
                } else numOfDishes++;

            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
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
        if ((var)%50==0)
            return var;
        else
            return getRndPrs50 (maxValue);

    }
    static int randomPrice() {
        return getRndPrs50 (1001);
    }
    static int randomWeight() {
        return getRndPrs50 (651);
    }

    static String randomName() {
        return NAMES[RND.nextInt(NAMES.length)];
    }
    static boolean randomDiscount() {
        return DISCOUNTS[RND.nextInt(DISCOUNTS.length)];
    }



}
