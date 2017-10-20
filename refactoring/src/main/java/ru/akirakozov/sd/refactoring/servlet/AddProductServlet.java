package ru.akirakozov.sd.refactoring.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.akirakozov.sd.refactoring.utils.DBHelper;
import ru.akirakozov.sd.refactoring.utils.ResponseHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        ResponseHelper.createResponse(response -> response.getWriter().println("OK"), resp);
    }

    private void insertIntoDatabase(String name, long price) {
        DBHelper.queryDatabase((stmt) -> {
            String sql = "INSERT INTO PRODUCT " +
                    "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
            stmt.executeUpdate(sql);
            logger.info("Inserted a row with values name = {}, price = {}", name, price);
            return null;
        });
    }
}
