package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class JdbcTransfersDao {
    private JdbcTemplate jdbcTemplate;
    private AuthenticatedUser currentUser;

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

    public Transfer getTransfersByTransferId(int transferId){
        Transfer newTransfer = null;
        String sql = "SELECT transfers.transfer_id, transfers.transfer_type_id, transfers.transfer_status_id, " +
                "transfers.account_from, transfers.account_to, transfers.amount, transfer_types.transfer_type_desc, " +
                "transfer_statuses.transfer_status_desc " +
                "FROM transfers " +
                "JOIN transfer_types " +
                "ON transfers.transfer_type_id = transfer_types.transfer_type_id " +
                "JOIN transfer_statuses " +
                "ON transfers.transfer_status_id = transfer_statuses.transfer_status_id " +
                "WHERE transfers.transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        while (results.next()) {
            newTransfer = mapRowToTransfer(results);
        }
        return newTransfer;
    }

    private Transfer sendTransfer (String chosenUsername, BigDecimal amount){
        Transfer newTransfer = null;

        String checkBalanceSql = "SELECT balance " +
                "FROM account " +
                "WHERE user_id = (SELECT user_id FROM users WHERE username = ?);";
        BigDecimal currentBalance = jdbcTemplate.queryForObject(checkBalanceSql, currentUser.getUsername);

        if (amount.compareTo(currentBalance) <= 0) {
            String typeSql = "INSERT INTO transfer_types (transfer_status_desc) " +
                    "VALUES ('sent') " +
                    "RETURNING transfer_type_id;";
            int transferTypeId = jdbcTemplate.queryForObject(typeSql, int.class);

            String statusSql = "INSERT INTO transfer_statuses (transfer_status_desc) " +
                    "VALUES ('approve') " +
                    "RETURNING transfer_status_id;";
            int transferStatusId = jdbcTemplate.queryForObject(statusSql, int.class);

            String transferSql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (?, ?, ?) " +
                    "RETURNING transfer_id;";
            int transferId = jdbcTemplate.queryForObject(transferSql, currentUser.getUsername(), chosenUsername, amount);

            String takeFromSenderSql = "UPDATE accounts " +
                    "SET balance -= ? " +
                    "WHERE user_id = (SELECT user_id FROM users WHERE username = ?);";
            jdbcTemplate.update(takeFromSenderSql, amount, currentUser.getUsername);

            String giveToReceiverSql = "UPDATE accounts " +
                    "SET balance += ? " +
                    "WHERE user_id = (SELECT user_id FROM users WHERE username = ?);";
            jdbcTemplate.update(takeFromSenderSql, amount, chosenUsername);

            newTransfer = getTransfersByTransferId(transferId);
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
