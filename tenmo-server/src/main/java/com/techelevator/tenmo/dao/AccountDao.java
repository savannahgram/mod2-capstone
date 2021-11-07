package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

BigDecimal[] findBalance(String username);

Account getAccount(String username);
}
