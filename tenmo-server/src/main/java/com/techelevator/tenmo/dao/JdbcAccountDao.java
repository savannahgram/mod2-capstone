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
public class JdbcAccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal[] findBalance(String username) {
        BigDecimal[] result = null;
        String sql = "SELECT balance FROM accounts WHERE user_id = (SELECT user_id FROM users WHERE username = ?)";
        BigDecimal[] balances = jdbcTemplate.queryForObject(sql, BigDecimal[].class, username);
        if (balances != null){
            result = balances;
        }
        return result;
    }



    public Account getAccount(String username){
        Account account = null;
        String sql = "SELECT accounts.account_id, accounts.user_id, accounts.balance " +
                "FROM accounts " +
                "JOIN users " +
                "ON accounts.user_id = users.user_id " +
                "WHERE users.username = ? " +
                "LIMIT 1;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
        while (results.next()) {
            account.mapRowToAccount(results);
        }
        return account;
    }



}
