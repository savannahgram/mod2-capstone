package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JdbcTransfersDao {

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

    public List<Transfer> getTransfersByTransferId(int transferId){
        List<Transfer> transfers = null;
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
            Transfer newTransfer = mapRowToTransfer(results);
            transfers.add(newTransfer);
        }
        return transfers;
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
