package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountsDao;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsController {

    private AccountsDao accountsDao;

    // get balance for all accounts belonging to user
    //problem check url
    @RequestMapping(path = "", method = RequestMethod.GET)
    public String balanceStatement() {
        return accountsDao.getBalance();
    }

}
