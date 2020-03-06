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


@WebServlet("/sendMoney")
public class SendMoney extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sumOfMoneyStr = req.getParameter("sumOfMoney");
        String idAccountFromStr = req.getParameter("idAccountFrom");
        String idAccountToStr = req.getParameter("idAccountTo");

        Float sumOfMoney = Float.parseFloat (sumOfMoneyStr);
        Long idAccountFrom = Long.parseLong (idAccountFromStr);
        Long idAccountTo = Long.parseLong (idAccountToStr);

        System.out.println ("sumOfMoney = "+sumOfMoney);
        System.out.println ("idAccountFrom = "+idAccountFrom);
        System.out.println ("idAccountTo = "+idAccountTo);


        System.out.println ("--doGET--");

        dbService dbService1 = new dbService ();
        dbService1.init ();

        BankAccount bankAccountFrom = dbService1.getBankAccount (idAccountFrom);
        BankAccount bankAccountTo = dbService1.getBankAccount (idAccountTo);

        /*dbService1.sendMoney (bankAccountFrom,bankAccountTo, sumOfMoney);*/

        System.out.println (bankAccountFrom);
        System.out.println (bankAccountTo);

        String login = bankAccountFrom.getUser ().getLogin ();
        String pass = bankAccountFrom.getUser ().getPass ();
        User user = dbService1.getUser (login, pass);
        dbService1.finish ();
        req.setAttribute("user", user);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/myBankMenu.jsp");
        dispatcher.forward(req, resp);

    }
}
