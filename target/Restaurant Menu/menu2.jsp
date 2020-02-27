<%@ page import="kiev.vlad.SimpleDish" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>JSTL Sample</title>
  </head>
  <body>
      <a href="/list2">Click</a> to get ALL orders list.<br/>
      <a href="/list2?discount=true">Click</a> to get orders with discount<br/>
      <a href="/list2?minPrice=100">Click</a> to get orders where min price = 100<br/>
      <a href="/list2?maxPrice=1000">Click</a> to get orders where max price = 1000<br/>
      <a href="/list2?minPrice=&maxPrice=&discount=">Click</a> to get orders where max price = 1000, min price = 100 and have a discount<br/>




      <form action="/list2" method="GET">
          <h2>FILTER</h2><br>
          minPrice: <input type="text" name="minPrice" ><br>
          maxPrice: <input type="text" name="maxPrice" ><br>
          <input type="checkbox" name="discount" value="true" /> have a discount<br />
          <input type="checkbox" name="massLow1KG" value="true" /> summary mass less than 1 Kg<br />
          <input type="submit"/>
      </form>


      <h1>Filtered dishes list:</h1>

      <table border="1">
          <%
              List<SimpleDish> list = (List<SimpleDish>) request.getAttribute ("dishList");
          %>
          <tr>
              <th>#</th>
              <th>name</th>
              <th>price</th>
              <th>weight</th>
              <th>discount</th>
              <th>function</th>
          </tr>

          <% if (list!=null) {
              for (SimpleDish dish: list) {  %>

          <tr>
              <td><% out.print(dish.getId());         %></td>
              <td><% out.print(dish.getName());       %></td>
              <td><% out.print(dish.getPrice());      %></td>
              <td><% out.print(dish.getWeight());     %></td>
              <td><% out.print(dish.isDiscount());    %></td>
              <td><a href="/dishs?id=<% out.print(dish.getId ()); %>"> add to basket</a></td>
          </tr>
              <% System.out.println (dish.toString ());
              }
          }  %>
      </table>



  </body>
</html>
