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

public class GetProductsServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(GetProductsServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> productsFromDatabase = getProductsFromDatabase();

        createResponse(resp, productsFromDatabase);
    }

    private void createResponse(HttpServletResponse resp, List<String> productsFromDatabase) {
        try {
            resp.getWriter().println("<html><body>");

            productsFromDatabase.forEach(resp.getWriter()::println);

            if (productsFromDatabase.isEmpty()) {
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

    private List<String> getProductsFromDatabase() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
                Statement statement = c.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCT");

            List<String> products = new ArrayList<>();

            QueryServlet.parseNamePrice(products, resultSet);

            return products;
        }
        catch (SQLException e) {
            logger.error("Error while getting products", e);
            throw new RuntimeException(e);
        }
    }
}
