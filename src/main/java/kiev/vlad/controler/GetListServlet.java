package kiev.vlad.controler;

import kiev.vlad.SimpleDish;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/list2")
public class GetListServlet extends HttpServlet {
    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            String discountStr = req.getParameter ("discount");
            if (discountStr == "") discountStr = null;
            String minPriceStr = req.getParameter ("minPrice");
            if (minPriceStr == "") minPriceStr = null;
            String maxPriceStr = req.getParameter ("maxPrice");
            if (maxPriceStr == "") maxPriceStr = null;
            String massLow1KgStr = req.getParameter ("massLow1KG");
            if (massLow1KgStr == "") discountStr = null;

            System.out.println ("--/list2.doGET--");

            System.out.println ("discountStr = " + discountStr);
            System.out.println ("minPriceStr = " + minPriceStr);
            System.out.println ("maxPriceStr = " + maxPriceStr);
            System.out.println ("massLow1KgStr = " + massLow1KgStr);

            List<SimpleDish> list = DishService.filter (discountStr, minPriceStr, maxPriceStr, massLow1KgStr);

            req.setAttribute ("dishList", list);

            RequestDispatcher dispatcher = getServletContext ().getRequestDispatcher ("/menu2.jsp");
            dispatcher.forward (req, resp);

    }
}
