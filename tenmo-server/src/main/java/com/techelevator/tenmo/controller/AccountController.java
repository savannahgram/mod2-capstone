package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@PreAuthorize("isAuthenticated()")
@RequestMapping("/accounts")
@RestController
public class AccountController {

    private AccountDao accountDao;

    public AccountController (AccountDao accountDao){
        this.accountDao = accountDao;
    }


    @RequestMapping(path = "/user/{username}", method = RequestMethod.GET)
    public Account getAccount(@PathVariable String username){
        return accountDao.getAccount(username);
    }

}
