package ru.akirakozov.sd.refactoring.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AddProductServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(AddProductServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        long price = Long.parseLong(req.getParameter("price"));

        insertIntoDatabase(name, price);
        createResponse(resp);
    }

    private void createResponse(HttpServletResponse resp) {
        try {
            resp.setContentType("text/html");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("OK");
        } catch (IOException e) {
            logger.error("Error while creating a response", e);
            throw new RuntimeException(e);
        }
    }

    private void insertIntoDatabase(String name, long price) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
                Statement statement = c.createStatement()) {
            String sql = "INSERT INTO PRODUCT " +
                    "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
            statement.executeUpdate(sql);
            logger.info("Inserted a row with values name = {}, price = {}", name, price);
        }
        catch (SQLException e) {
            logger.error("Error while inserting into database", e);
            throw new RuntimeException(e);
        }
    }
}
