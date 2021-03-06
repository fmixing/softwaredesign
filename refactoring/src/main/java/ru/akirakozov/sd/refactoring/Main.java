package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;
import ru.akirakozov.sd.refactoring.utils.DBHelper;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        createDatabase();
        startServer();
    }

    private static void startServer() {
        try {
            Server server = new Server(8081);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);

            context.addServlet(new ServletHolder(new AddProductServlet()), "/add-product");
            context.addServlet(new ServletHolder(new GetProductsServlet()),"/get-products");
            context.addServlet(new ServletHolder(new QueryServlet()),"/query");

            server.start();
            server.join();
        }
        catch (Exception e) {
            logger.error("Error while starting server", e);
            throw new RuntimeException(e);
        }
    }

    private static void createDatabase() {
        DBHelper.queryDatabase((stmt) -> {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";

            stmt.executeUpdate(sql);
            return null;
        });
    }
}
