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

public class GetProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> productsFromDatabase = getProductsFromDatabase();

        createResponse(resp, productsFromDatabase);
    }

    private void createResponse(HttpServletResponse resp, List<String> productsFromDatabase) {
        ResponseHelper.createResponse(response -> {
            response.getWriter().println("<html><body>");

            productsFromDatabase.forEach(response.getWriter()::println);

            if (productsFromDatabase.isEmpty()) {
                response.getWriter().println("No products are found");
            }

            response.getWriter().println("</body></html>");
        }, resp);
    }

    private List<String> getProductsFromDatabase() {
        return DBHelper.queryDatabase((stmt) -> {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM PRODUCT");

            List<String> products = new ArrayList<>();

            DBHelper.parseNamePrice(products, resultSet);

            return products;
        });
    }
}
