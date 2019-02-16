package org.simple.dao;

import org.simple.model.User;
import org.simple.utils.Constants;
import org.simple.utils.LoggerUtils;

import java.sql.*;

public class UserServiceDao {

    static Connection userServiceDbConn = null;

    public UserServiceDao() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            LoggerUtils.logMessage("MySQL JDBC Driver Registered!");
        } catch (ClassNotFoundException e) {
            LoggerUtils.logMessage("Please check to add JDBC Maven Dependency");
            e.printStackTrace();
            return;
        }
        boolean connectionCreated = false;
        while (!connectionCreated) {
            try {
                // DriverManager: The basic service for managing a set of JDBC drivers.
                userServiceDbConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "root");
                if (userServiceDbConn != null) {
                    LoggerUtils.logMessage("Connection Successful");
                    connectionCreated = true;
                } else {
                    LoggerUtils.logMessage("Failed to make connection. Trying again");
                }
            } catch (SQLException e) {
                LoggerUtils.logMessage("MySQL Connection Failed. Trying again");
                //e.printStackTrace();
            }
        }
    }

    public void addUser(User user) {
        LoggerUtils.logMessage(user + " adding ... ");
        try {
            String insertStatement = "INSERT  INTO  Users  VALUES  (?,?,?,?)";
            PreparedStatement preparedStatement = userServiceDbConn.prepareStatement(insertStatement);
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setInt(4, user.getAge());

            // execute insert SQL statement
            preparedStatement.executeUpdate();
            LoggerUtils.logMessage(user + " added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User fetchUser(String userId) {
        LoggerUtils.logMessage(userId + " fetching ... ");
        try {
            String selectStatement = "SELECT * FROM  Users  WHERE used_id=?";
            PreparedStatement preparedStatement = userServiceDbConn.prepareStatement(selectStatement);
            preparedStatement.setString(1, userId);

            // execute select SQL statement
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.getFetchSize() == 0) {
                LoggerUtils.logMessage("No user found with id " + userId);
                return null;
            }
            User user = new User();
            user.setUserId(resultSet.getString(Constants.USER_ID));
            user.setFirstName(resultSet.getString(Constants.LAST_NAME));
            user.setLastName(resultSet.getString(Constants.LAST_NAME));
            user.setAge(resultSet.getInt(Constants.AGE));
            LoggerUtils.logMessage(user + " fetched successfully");
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateUser(User user) {
        LoggerUtils.logMessage(user + " updating ... ");
        try {
            String updateStatement = "UPDATE Users SET first_name=?, last_name=?, age=? WHERE user_id=?";
            PreparedStatement preparedStatement = userServiceDbConn.prepareStatement(updateStatement);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setInt(3, user.getAge());
            preparedStatement.setString(4, user.getUserId());

            // execute update SQL statement
            preparedStatement.executeUpdate();
            LoggerUtils.logMessage(user + " updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String userId) {
        LoggerUtils.logMessage(userId + " deleting ... ");
        try {
            String updateStatement = "DELETE FROM Users WHERE user_id=?";
            PreparedStatement preparedStatement = userServiceDbConn.prepareStatement(updateStatement);
            preparedStatement.setString(1, userId);

            // execute delete SQL statement
            preparedStatement.executeUpdate();
            LoggerUtils.logMessage(userId + " deleted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
