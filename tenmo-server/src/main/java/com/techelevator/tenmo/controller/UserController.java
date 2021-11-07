package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequestMapping("/user/")
@RestController
public class UserController {
    private UserDao userDao;

    @RequestMapping(path = "all/", method = RequestMethod.GET)
    public List<User> findAll(){
        return userDao.findAll();
    }

    @RequestMapping(path = "byusername/", method = RequestMethod.GET)
    public User findByUsername(String username){
        return userDao.findByUsername(username);
    }

    @RequestMapping(path = "byid/", method = RequestMethod.GET)
    public User findById(int id){
        return userDao.findById(id);
    }

    @RequestMapping(path = "findid/", method = RequestMethod.GET)
    public int findIdByUsername(String username){
        return userDao.findIdByUsername(username);
    }




}
