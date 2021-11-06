package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountsDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountsController {

    private AccountsDao accountsDao;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public String balanceStatement() {
        return accountsDao.getBalance();
    }

}
