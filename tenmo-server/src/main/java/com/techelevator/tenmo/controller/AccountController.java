package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;

@PreAuthorize("isAuthenticated()")
@RequestMapping("/account/")
@RestController
public class AccountController {

    private AccountDao accountDao;

    @RequestMapping(path = "balance/", method = RequestMethod.GET)
    public BigDecimal[] findBalance(Principal currentUser) {
        return accountDao.findBalance(currentUser.getName());
    }

    @RequestMapping(path = "user/{username}", method = RequestMethod.GET)
    public Account getAccount(@PathVariable String username){
        return accountDao.getAccount(username);
    }

}
