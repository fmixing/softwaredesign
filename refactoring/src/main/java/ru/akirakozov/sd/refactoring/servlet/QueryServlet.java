package ru.akirakozov.sd.refactoring.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QueryServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(QueryServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String command = req.getParameter("command");

        Optional<List<String>> strings = queryDatabase(command);

        if (!strings.isPresent()) {
            createUnknownCommandResponse(resp, command);
        }
        else {
            createResponse(resp, strings.get(), command);
        }
    }

    private void createUnknownCommandResponse(HttpServletResponse resp, String command) {
        try {
            resp.getWriter().println("Unknown command: " + command);
            resp.setContentType("text/html");
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            logger.error("Error while creating a response", e);
            throw new RuntimeException(e);
        }
    }

    private void createResponse(HttpServletResponse resp, List<String> results, String command) {
        try {
            resp.getWriter().println("<html><body>");

            switch (command) {
                case "max" :
                    resp.getWriter().println("<h1>Product with max price: </h1>");
                    break;
                case "min" :
                    resp.getWriter().println("<h1>Product with min price: </h1>");
                    break;
                case "sum" :
                    resp.getWriter().println("Summary price: ");
                    break;
            }
            results.forEach(resp.getWriter()::println);
            if (results.isEmpty()) {
                resp.getWriter().println("No products are found");
            }
            resp.getWriter().println("</body></html>");
            resp.setContentType("text/html");
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (IOException e) {
            logger.error("Error while creating a response", e);
            throw new RuntimeException(e);
        }
    }

    private Optional<List<String>> queryDatabase(String command) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
                Statement stmt = c.createStatement()) {
            List<String> ans = new ArrayList<>();
            ResultSet resultSet;
            switch (command) {
                case "max" :
                    resultSet = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
                    parseNamePrice(ans, resultSet);
                    break;
                case "min" :
                    resultSet = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
                    parseNamePrice(ans, resultSet);
                    break;
                case "sum" :
                    resultSet = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");
                    ans.add(resultSet.next() ? resultSet.getString(1) : "0");
                    break;
                default :
                    return Optional.empty();
            }
            return Optional.of(ans);
        }
        catch (SQLException e) {
            logger.error("Error while querying database", e);
            throw new RuntimeException(e);
        }
    }

    static void parseNamePrice(List<String> ans, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String  name = resultSet.getString("name");
            int price  = resultSet.getInt("price");
            ans.add(name + "\t" + price + "</br>");
        }
    }

}
