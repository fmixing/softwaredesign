package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.utils.DBHelper;
import ru.akirakozov.sd.refactoring.utils.ResponseHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QueryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String command = req.getParameter("command");

        if (isKnownCommand(command)) {
            List<String> strings = queryDatabase(command);
            createResponse(resp, strings, command);
        }
        else {
            createUnknownCommandResponse(resp, command);
        }
    }

    private boolean isKnownCommand(String command) {
        return command.equals("max") || command.equals("min") || command.equals("sum");
    }

    private void createUnknownCommandResponse(HttpServletResponse resp, String command) {
        ResponseHelper.createResponse(response -> response.getWriter().println("Unknown command: " + command), resp);
    }

    private void createResponse(HttpServletResponse resp, List<String> results, String command) {
        ResponseHelper.createResponse(response -> {
            response.getWriter().println("<html><body>");

            switch (command) {
                case "max" :
                    response.getWriter().println("<h1>Product with max price: </h1>");
                    break;
                case "min" :
                    response.getWriter().println("<h1>Product with min price: </h1>");
                    break;
                case "sum" :
                    response.getWriter().println("Summary price: ");
                    break;
            }
            results.forEach(response.getWriter()::println);
            if (results.isEmpty()) {
                response.getWriter().println("No products are found");
            }
            response.getWriter().println("</body></html>");
        }, resp);
    }

    private List<String> queryDatabase(String command) {
        return DBHelper.queryDatabase((stmt) -> {
            List<String> ans = new ArrayList<>();
            ResultSet resultSet;
            switch (command) {
                case "max" :
                    resultSet = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
                    DBHelper.parseNamePrice(ans, resultSet);
                    break;
                case "min" :
                    resultSet = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
                    DBHelper.parseNamePrice(ans, resultSet);
                    break;
                case "sum" :
                    resultSet = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");
                    ans.add(String.valueOf(resultSet.getInt(1)));
                    break;
                default :
                    throw new IllegalStateException();
            }
            return ans;
        });
    }

}
