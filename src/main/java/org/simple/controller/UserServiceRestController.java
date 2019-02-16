package org.simple.controller;

import org.simple.dao.UserServiceDao;
import org.simple.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserServiceRestController {

    UserServiceDao userServiceDao;

    UserServiceRestController() {
        userServiceDao = new UserServiceDao();
    }

    @Autowired
    private User person;

    @RequestMapping("/")
    public String healthCheck() {
        return "OK";
    }

    @RequestMapping("/user/get")
    public User getUser(@RequestParam(name = "user_id", required = false, defaultValue = "Unknown") String userId) {
        User user = userServiceDao.fetchUser(userId);
        return user;
    }

    @RequestMapping("/user/delete")
    public void deleteUser(@RequestParam(name = "user_id", required = false, defaultValue = "Unknown") String userId) {
        userServiceDao.deleteUser(userId);
    }


    @RequestMapping(value = "/user/update", method = RequestMethod.POST, consumes = "application/json")
    public User updatePerson(@RequestBody User userReceived) {
        User user = userServiceDao.fetchUser(userReceived.getUserId());
        if (user != null) {
            userServiceDao.updateUser(user);
        } else {
            userServiceDao.addUser(user);
        }
        return userReceived;
    }
}
