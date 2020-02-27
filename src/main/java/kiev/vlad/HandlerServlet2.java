package kiev.vlad;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/list")
public class HandlerServlet2 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String discountStr = req.getParameter("discount");
        String minPriceStr = req.getParameter("minPrice");
        String maxPriceStr = req.getParameter("maxPrice");

        System.out.println ("--doGET--");

        /*System.out.println ("discountStr = " + discountStr);
        System.out.println ("minPriceStr = " + minPriceStr);
        System.out.println ("maxPriceStr = " + maxPriceStr);*/

        SimpleFilter sf = new SimpleFilter ();

        List<SimpleDish> list = sf.paramForFiltering (discountStr, minPriceStr, maxPriceStr);

        req.setAttribute("dishList", list);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/menu.jsp");
        dispatcher.forward(req, resp);

    }
}
