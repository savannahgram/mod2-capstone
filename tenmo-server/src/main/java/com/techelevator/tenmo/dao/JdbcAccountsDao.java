package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountsDao {

    private static final BigDecimal STARTING_BALANCE = new BigDecimal("1000.00");
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String showAsDollars(BigDecimal money){
        String stringMath = "$" + String.format("%.2f",money);
        return stringMath;
    }

    public String getBalance(int user_id) {
        String sql = "SELECT balance FROM accounts WHERE user_id = ?";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, user_id);
        if (balance != null){
            return "balance is: " + showAsDollars(balance);
        }
        else {
            return "balance is null";
        }
    }
}
