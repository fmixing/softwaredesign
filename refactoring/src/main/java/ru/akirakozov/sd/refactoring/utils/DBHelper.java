package ru.akirakozov.sd.refactoring.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public class DBHelper {

    private static final Logger logger = LoggerFactory.getLogger(DBHelper.class);

    public static void parseNamePrice(List<String> ans, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String  name = resultSet.getString("name");
            int price  = resultSet.getInt("price");
            ans.add(name + "\t" + price + "</br>");
        }
    }

    public static <T> T queryDatabase(FuncWithException<Statement, T> querying) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
             Statement statement = c.createStatement()) {
            return querying.apply(statement);
        }
        catch (SQLException e) {
            logger.error("Error while querying database", e);
            throw new RuntimeException(e);
        }
    }

    public interface FuncWithException <P, T> {
        T apply(P p) throws SQLException;
    }

}
