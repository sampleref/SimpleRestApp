package org.simple.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.simple.dao.UserServiceDao;
import org.simple.model.User;
import org.simple.utils.LoggerUtils;
import spark.Filter;
import spark.Spark;

import java.util.HashMap;

import static spark.Spark.*;

public class SparkUserServiceController {

    private static final HashMap<String, String> corsHeaders = new HashMap<String, String>();

    static {
        corsHeaders.put("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
        corsHeaders.put("Access-Control-Allow-Origin", "*");
        corsHeaders.put("Access-Control-Allow-Headers", "*");
        corsHeaders.put("Access-Control-Allow-Credentials", "true");
    }

    UserServiceDao userServiceDao;

    public SparkUserServiceController() {
        userServiceDao = new UserServiceDao();
    }

    public final static void applyCORS() {
        Filter filter = (request, response) -> corsHeaders.forEach((key, value) -> {
            response.header(key, value);
        });
        Spark.after(filter);
        Spark.before(filter);
    }

    public static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }

    public void attachHandlers() {

        SparkUserServiceController.enableCORS("*", "GET,PUT,POST,DELETE,OPTIONS", "*");

        post("/user/update", (req, res) -> {
            LoggerUtils.logMessage("Triggered /user/update");
            ObjectMapper mapper = new ObjectMapper();
            final User userReceived = mapper.readValue(req.body(), User.class);
            LoggerUtils.logMessage(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userReceived));
            User user = userServiceDao.fetchUser(userReceived.getUserId());
            if (user != null) {
                userServiceDao.updateUser(userReceived);
            } else {
                userServiceDao.addUser(userReceived);
            }
            return mapper.writeValueAsString(userReceived);
        });

        post("/user/get", (req, res) -> {
            LoggerUtils.logMessage("Triggered /user/get");
            ObjectMapper mapper = new ObjectMapper();
            final ObjectNode node = mapper.readValue(req.body(), ObjectNode.class);
            LoggerUtils.logMessage(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
            User user = userServiceDao.fetchUser(node.get("userId").asText(""));
            return user != null ? mapper.writeValueAsString(user) : mapper.writeValueAsString(new User());
        });

        post("/user/delete", (req, res) -> {
            LoggerUtils.logMessage("Triggered /user/delete");
            ObjectMapper mapper = new ObjectMapper();
            final ObjectNode node = mapper.readValue(req.body(), ObjectNode.class);
            LoggerUtils.logMessage(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
            userServiceDao.deleteUser(node.get("userId").asText(""));
            return "done";
        });
    }

}
