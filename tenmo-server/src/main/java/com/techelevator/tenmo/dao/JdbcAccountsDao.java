package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountsDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal[] getBalance(User userObject) {
        BigDecimal[] result = null;
        String sql = "SELECT balance FROM accounts WHERE user_id = ?";
        BigDecimal[] balances = jdbcTemplate.queryForObject(sql, BigDecimal[].class, userObject.getId());
        if (balances != null){
            result = balances;
        }
        return result;
    }



    public Account getAccount(String username){
        Account account = null;
        String sql = "SELECT account_id, user_id, balance " +
                "FROM accounts " +
                "JOIN user " +
                "ON accounts.user_id = user.user_id " +
                "WHERE user.username = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
        while (results.next()) {
            account.mapRowToAccount(results);
        }
        return account;
    }



}
