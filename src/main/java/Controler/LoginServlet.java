package Controler;


import model.BankAccount;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String pass = req.getParameter("pass");

        System.out.println ("login = "+login);
        System.out.println ("pass = "+pass);


        System.out.println ("--doGET--");
        dbService dbService1 = new dbService ();
        dbService1.init ();
        User user = dbService1.getUser (login, pass);

        List<BankAccount> bankAccountList = dbService1.getBankAccounts ();
        dbService1.finish ();
        System.out.println (user);
        //Object userInfo = " ";

        req.setAttribute("bankAccountList", bankAccountList);
        req.setAttribute("user", user);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/myBankMenu.jsp");
        dispatcher.forward(req, resp);

    }
}
