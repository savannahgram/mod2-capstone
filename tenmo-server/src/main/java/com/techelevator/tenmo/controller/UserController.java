package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequestMapping("/user")
@RestController
public class UserController {
    private UserDao userDao;

    public UserController (UserDao userDao){
        this.userDao = userDao;
    }

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public List<User> findAll(){
        return userDao.findAll();
    }

    @RequestMapping(path = "/byusername", method = RequestMethod.GET)
    public User findByUsername(String username){
        return userDao.findByUsername(username);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User findById(@PathVariable int id){
        return userDao.findById(id);
    }

    @RequestMapping(path = "/findid", method = RequestMethod.GET)
    public int findIdByUsername(String username){
        return userDao.findIdByUsername(username);
    }

    @RequestMapping(path = "/findbyaccount", method = RequestMethod.GET)
    public User findUserByAccountId(int id){
        return userDao.findUserByAccountId(id);
    }




}
