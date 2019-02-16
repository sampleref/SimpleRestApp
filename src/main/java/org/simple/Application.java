package org.simple;

import org.simple.controller.SparkUserServiceController;
import org.simple.utils.LoggerUtils;
import spark.Spark;

public class Application {

    public static void main(String[] args) {

        Spark.port(8081);
        SparkUserServiceController sparkUserServiceController = new SparkUserServiceController();
        sparkUserServiceController.attachHandlers();
        LoggerUtils.logMessage("Listening ... ");
        while (true) {

        }
    }
}
