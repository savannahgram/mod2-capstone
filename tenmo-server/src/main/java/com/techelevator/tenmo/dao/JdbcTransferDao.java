package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private JdbcTemplate jdbcTemplate;
    private User currentUser;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getTransfersByUsername(String username){
        List<Transfer> transfers = null;
        String sql = "SELECT transfers.transfer_id, transfers.transfer_type_id, transfers.transfer_status_id, " +
                "transfers.account_from, transfers.account_to, transfers.amount, transfer_types.transfer_type_desc, " +
                "transfer_statuses.transfer_status_desc " +
                "FROM transfers " +
                "JOIN transfer_types " +
                "ON transfers.transfer_type_id = transfer_types.transfer_type_id " +
                "JOIN transfer_statuses " +
                "ON transfers.transfer_status_id = transfer_statuses.transfer_status_id " +
                "WHERE transfers.account_from = (SELECT user_id FROM users WHERE username = ?) " +
                "OR transfers.account_from = (SELECT user_id FROM users WHERE username = ?);";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
        while (results.next()) {
            Transfer newTransfer = mapRowToTransfer(results);
            transfers.add(newTransfer);
        }
        return transfers;
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
        Transfer newTransfer = null;
        //sql for knowing whether send or received, so which user based on boolean (if loop to change the parameter)
        String whichName = "account_to";
        if (findTransferType(transferId) == "Send"){
            whichName = "account_to";
        }
        String sql = "SELECT transfers.transfer_id, transfers.transfer_type_id, transfers.transfer_status_id, " +
                "transfers.account_from, transfers.account_to, transfers.amount, transfer_types.transfer_type_desc, " +
                "transfer_statuses.transfer_status_desc, users.username " +
                "FROM transfers " +
                "JOIN transfer_types " +
                "ON transfers.transfer_type_id = transfer_types.transfer_type_id " +
                "JOIN transfer_statuses " +
                "ON transfers.transfer_status_id = transfer_statuses.transfer_status_id " +
                "WHERE transfers.transfer_id = ? " +
                "JOIN users " +
                "ON transfers.? = users.user_id " +
                "LIMIT 1;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId, whichName);
        while (results.next()) {
            newTransfer = mapRowToTransfer(results);
        }
        return newTransfer;
    }

    private BigDecimal checkBalanceEnough(String currentUsername){
        String checkBalanceSql = "SELECT balance " +
                "FROM account " +
                "WHERE user_id = (SELECT user_id FROM users WHERE username = ?);";
        BigDecimal currentBalance = jdbcTemplate.queryForObject(checkBalanceSql, BigDecimal.class, currentUsername);
        return currentBalance;
    }

    private int addTransferType(){
        String typeSql = "INSERT INTO transfer_types (transfer_status_desc) " +
                "VALUES ('Send') " +
                "RETURNING transfer_type_id;";
        int transferTypeId = jdbcTemplate.queryForObject(typeSql, int.class);
        return transferTypeId;
    }

    private int addTransferStatus(){
        String statusSql = "INSERT INTO transfer_statuses (transfer_status_desc) " +
                "VALUES ('Approved') " +
                "RETURNING transfer_status_id;";
        int transferStatusId = jdbcTemplate.queryForObject(statusSql, int.class);
        return transferStatusId;
    }

    private void takeFromSender(BigDecimal amount, String currentUsername){
        String takeFromSenderSql = "UPDATE accounts " +
                "SET balance -= ? " +
                "WHERE user_id = (SELECT user_id FROM users WHERE username = ?);";
        jdbcTemplate.update(takeFromSenderSql, amount, currentUsername);
    }

    private void giveToReceiver(BigDecimal amount, String chosenUsername){
        String giveToReceiverSql = "UPDATE accounts " +
                "SET balance += ? " +
                "WHERE user_id = (SELECT user_id FROM users WHERE username = ?);";
        jdbcTemplate.update(giveToReceiverSql, amount, chosenUsername);
    }

    @Override
    public Transfer sendTransfer (String chosenUsername, BigDecimal amount, String currentUsername){
        Transfer newTransfer = null;

        if (amount.compareTo(checkBalanceEnough(currentUsername)) <= 0) {
            String transferSql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "RETURNING transfer_id;";
            int transferId = jdbcTemplate.queryForObject(transferSql, int.class, addTransferType(), addTransferStatus(), currentUsername, chosenUsername, amount);

            takeFromSender(amount, currentUsername);
            giveToReceiver(amount, chosenUsername);

            newTransfer = getTransferByTransferId(transferId);
        }
        return newTransfer;
    }


    public Transfer mapRowToTransfer (SqlRowSet result) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(result.getInt("transfers.transfer_id"));
        transfer.setTransferTypeId(result.getInt("transfers.transfers_type_id"));
        transfer.setTransferTypeDesc(result.getString("transfer_types.transfer_type_desc"));
        transfer.setTransferStatusId(result.getInt("transfers.transfers_status_id"));
        transfer.setTransferStatusDesc(result.getString("transfer_statuses.transfer_status_desc"));
        transfer.setAccountFrom(result.getInt("transfers.account_from"));
        transfer.setAccountTo(result.getInt("transfers.account_to"));
        transfer.setAmount(result.getBigDecimal("transfers.amount"));
        return transfer;
    }
}
