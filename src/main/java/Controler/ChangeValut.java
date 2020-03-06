package Controler;

import model.BankAccount;
import model.User;
import model.Valuta;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/changeValut")
public class ChangeValut extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String valuta = req.getParameter("valuta");
        String idAccountStr = req.getParameter("idAccount");


        Long idAccount = Long.parseLong (idAccountStr);

        System.out.println ("valuta = "+valuta);
        System.out.println ("idAccount = "+idAccount);


        System.out.println ("--doGET--");

        dbService dbService1 = new dbService ();
        dbService1.init ();
        BankAccount bankAccount = dbService1.getBankAccount (idAccount);

        dbService1.changeValutOfBankAccount (bankAccount, valuta);


        System.out.println (bankAccount);

        String login = bankAccount.getUser ().getLogin ();
        String pass = bankAccount.getUser ().getPass ();
        User user = dbService1.getUser (login, pass);
        dbService1.finish ();
        req.setAttribute("user", user);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/myBankMenu.jsp");
        dispatcher.forward(req, resp);

    }
}
