package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private JdbcTemplate jdbcTemplate;
    private User currentUser;
    private JdbcAccountDao jdbcAccountDao;
    private JdbcUserDao jdbcUserDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, JdbcAccountDao jdbcAccountDao, JdbcUserDao jdbcUserDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcAccountDao = jdbcAccountDao;
        this.jdbcUserDao = jdbcUserDao;
    }

    @Override
    public Transfer[] getTransfersByUsername(String username){
        List<Transfer> transfersList = new ArrayList<>();
        String sql = "SELECT transfers.transfer_id, transfers.transfer_type_id, transfers.transfer_status_id, " +
                "transfers.account_from, transfers.account_to, transfers.amount, transfer_types.transfer_type_desc, " +
                "transfer_statuses.transfer_status_desc " +
                "FROM transfers " +
                "JOIN transfer_types " +
                "ON transfers.transfer_type_id = transfer_types.transfer_type_id " +
                "JOIN transfer_statuses " +
                "ON transfers.transfer_status_id = transfer_statuses.transfer_status_id " +
                "WHERE transfers.account_from = (SELECT account_id FROM accounts JOIN users ON accounts.user_id = users.user_id WHERE users.username = ?) " +
                "OR transfers.account_from = (SELECT account_id FROM accounts JOIN users ON accounts.user_id = users.user_id WHERE users.username = ?);";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username, username);
        while (results.next()) {
            Transfer newTransfer = mapRowToTransfer(results);
            transfersList.add(newTransfer);
        }
        Transfer[] transferArray = new Transfer[transfersList.size()];
        for (int i = 0; i < transferArray.length; i ++){
            transferArray[i] = transfersList.get(i);
        }
        return transferArray;
    }

    private String findTransferType(int transferId){
        String transferType = null;
        String sql = "SELECT transfer_types.transfer_type_desc " +
                "FROM transfer_types " +
                "JOIN transfers " +
                "ON transfer_types.transfer_type_id = transfers.transfer_type_id " +
                "WHERE transfers.transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        transferType = results.getString("transfer_types.transfer_type_desc");
        return transferType;
    }

    @Override
    public Transfer getTransferByTransferId(int transferId){
        Transfer newTransfer = new Transfer();

        String sql = "SELECT transfers.transfer_id, transfers.transfer_type_id, transfers.transfer_status_id, " +
                "transfers.account_from, transfers.account_to, transfers.amount, transfer_types.transfer_type_desc, " +
                "transfer_statuses.transfer_status_desc " +
                "FROM transfers " +
                "JOIN transfer_types " +
                "ON transfers.transfer_type_id = transfer_types.transfer_type_id " +
                "JOIN transfer_statuses " +
                "ON transfers.transfer_status_id = transfer_statuses.transfer_status_id " +
                "WHERE transfers.transfer_id = ? " +
                "LIMIT 1;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        while (results.next()) {
            newTransfer = mapRowToTransfer(results);
        }
        return newTransfer;
    }

    private BigDecimal checkBalanceEnough(String currentUsername){
        String checkBalanceSql = "SELECT balance " +
                "FROM accounts " +
                "WHERE user_id = (SELECT user_id FROM users WHERE username = ?);";
        BigDecimal currentBalance = jdbcTemplate.queryForObject(checkBalanceSql, BigDecimal.class, currentUsername);
        return currentBalance;
    }

    public int getTransferTypeId(String typeDesc){
        String typeSql = "SELECT transfer_type_id " +
                "FROM transfer_types " +
                "WHERE transfer_type_desc = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(typeSql, typeDesc);
        int transferTypeId = 0;
        if (results.next()){
            transferTypeId = results.getInt("transfer_type_id");
        }
        return transferTypeId;
    }

    public int getTransferStatusId(String statusDesc){
        String statusSql = "SELECT transfer_status_id " +
                "FROM transfer_statuses " +
                "WHERE transfer_status_desc = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(statusSql, statusDesc);
        int statusId = 0;
        if (results.next()){
            statusId = results.getInt("transfer_status_id");
        }
        return statusId;
    }

    private void takeFromSender(BigDecimal amount, String currentUsername){
        String takeFromSenderSql = "UPDATE accounts " +
                "SET balance = balance - ? " +
                "WHERE user_id = (SELECT user_id FROM users WHERE username = ?);";
        jdbcTemplate.update(takeFromSenderSql, amount, currentUsername);
    }

    private void giveToReceiver(BigDecimal amount, String chosenUsername){
        String giveToReceiverSql = "UPDATE accounts " +
                "SET balance = balance + ? " +
                "WHERE user_id = (SELECT user_id FROM users WHERE username = ?);";
        jdbcTemplate.update(giveToReceiverSql, amount, chosenUsername);
    }

    @Override
    public Transfer sendTransfer (String chosenUsername, BigDecimal amount, String currentUsername){
        Transfer newTransfer = null;
        int chosenAccountId = jdbcAccountDao.getAccount(chosenUsername).getAccountId();
        int currentAccountId = jdbcAccountDao.getAccount(currentUsername).getAccountId();
        if (amount.compareTo(checkBalanceEnough(currentUsername)) <= 0) {
            String transferSql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "RETURNING transfer_id;";
            Integer transferId = jdbcTemplate.queryForObject(transferSql, Integer.class, getTransferTypeId("Send"),
                    getTransferStatusId("Approved"), currentAccountId, chosenAccountId, amount);

            takeFromSender(amount, currentUsername);
            giveToReceiver(amount, chosenUsername);

            newTransfer = getTransferByTransferId(transferId);
        }
        return newTransfer;
    }


    public Transfer mapRowToTransfer (SqlRowSet result) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(result.getInt("transfer_id"));
        transfer.setTransferTypeId(result.getInt("transfer_type_id"));
        transfer.setTransferTypeDesc(result.getString("transfer_type_desc"));
        transfer.setTransferStatusId(result.getInt("transfer_status_id"));
        transfer.setTransferStatusDesc(result.getString("transfer_status_desc"));
        transfer.setAccountFrom(result.getInt("account_from"));
        transfer.setAccountTo(result.getInt("account_to"));
        transfer.setAmount(result.getBigDecimal("amount"));
        return transfer;
    }
}
