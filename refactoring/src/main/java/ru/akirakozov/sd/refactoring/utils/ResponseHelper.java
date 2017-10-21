package ru.akirakozov.sd.refactoring.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseHelper {

    private static final Logger logger = LoggerFactory.getLogger(ResponseHelper.class);

    public static void createResponse(SuppWithException<HttpServletResponse> creator, HttpServletResponse resp) {
        try {
            resp.setContentType("text/html");
            resp.setStatus(HttpServletResponse.SC_OK);
            creator.apply(resp);
        } catch (IOException e) {
            logger.error("Error while creating a response", e);
            throw new RuntimeException(e);
        }
    }

    public interface SuppWithException <T> {
        void apply(T t) throws IOException;
    }
}
