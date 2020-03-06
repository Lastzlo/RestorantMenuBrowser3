
<%@ page import="java.util.List" %>
<%@ page import="model.User" %>
<%@ page import="model.BankAccount" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>MyBank</title>
  </head>
  <body>
    <h2>MY BANK INDEX</h2><br>

    <form action="/loginServlet" method="GET">
      <h3>Authorisation</h3><br>
      login: <input type="text" name="login" ><br>
      pass: <input type="text" name="pass" ><br>
      <input type="submit"/>
    </form>

    <%
      User user = (User) request.getAttribute ("user");
      List<BankAccount> bankAccountList = (List<BankAccount>) request.getAttribute ("bankAccountList");
    %>
    <%
      if (user==null) {
    %>

      <h3>You can authorize</h3>

    <%
      } else {
    %>

      <h2>My info</h2><br>

      <h3>Bio</h3>
      <table border="1">
        <tr>
          <th>idUser</th>
          <th>name</th>
          <th>pass</th>
        </tr>
        <tr>
          <td><% out.print(user.getId());         %></td>
          <td><% out.print(user.getLogin ());         %></td>
          <td><% out.print(user.getPass ());         %></td>

        </tr>
      </table>

      <%
        if (user.getBankAccounts ().size ()>0) {
          List<BankAccount> bankAccounts = user.getBankAccounts ();
      %>
        <h3>My accounts</h3><br>

        <table border="1">
          <tr>
            <th>idAccount</th>
            <th>valuta</th>
            <th>amountOfMoney</th>
            <%--<th>change valuta</th>--%>
          </tr>

          <%
            for (BankAccount bankAccount: bankAccounts) {
          %>
            <tr>
              <td><% out.print(bankAccount.getId ());                 %></td>
              <td><% out.print(bankAccount.getValuta ().getName ());  %></td>
              <td><% out.print(bankAccount.getAmountOfMoney ());      %></td>

            </tr>
          <%
            }
          %>

        </table>

      <%
        }
      %>

      <%
        if (bankAccountList.size ()>0) {
      %>
        <h3>All accounts</h3><br>

        <table border="1">
          <tr>
            <th>idAccount</th>
            <th>user</th>
            <th>valuta</th>
            <th>amountOfMoney</th>
          </tr>

          <%
            for (BankAccount bankAccount: bankAccountList) {
          %>
          <tr>
            <td><% out.print(bankAccount.getId ());                 %></td>
            <td><% out.print(bankAccount.getUser ().getLogin ());   %></td>
            <td><% out.print(bankAccount.getValuta ().getName ());  %></td>
            <td><% out.print(bankAccount.getAmountOfMoney ());      %></td>

          </tr>
          <%
            }
          %>

        </table>

      <%
        }
      %>

    <h2>My functions</h2><br>
    <table border="1">
      <tr>
        <th>change valuta</th>
        <th>send money</th>
      </tr>
      <tr>
        <td>
          <form action="/changeValut" method="GET">
            idAccount: <input type="text" name="idAccount" ><br>
            <p> choose new valuta: </p>
            <input type="radio" name="valuta" value="UAH" /> UAH<br />
            <input type="radio" name="valuta" value="EUR" /> EUR<br />
            <input type="radio" name="valuta" value="USD" /> USD<br />
            <input type="submit"/>
          </form>
        </td>
        <td>
          <form action="/sendMoney" method="GET">
            idAccountFrom: <input type="text" name="idAccountFrom" ><br>
            idAccountTo: <input type="text" name="idAccountTo" ><br>
            sum of money: <input type="text" name="sumOfMoney" ><br>
            <input type="submit"/>
          </form>
        </td>
      </tr>
    </table>

    <%
      }
    %>




  </body>
</html>
