package com.techelevator.tenmo.model;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;

public class Transfer {
private int transferId;
private int transferTypeId;
private String transferTypeDesc;
private int transferStatusId;
private String transferStatusDesc;
private int accountFrom;
private int accountTo;
private BigDecimal amountTransferred;
private BigDecimal balanceOfFrom;
private BigDecimal balanceOfTo;

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public String getTransferTypeDesc() {
        return transferTypeDesc;
    }

    public void setTransferTypeDesc(String transferTypeDesc) {
        this.transferTypeDesc = transferTypeDesc;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }

    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmountTransferred() {
        return amountTransferred;
    }

    public void setAmountTransferred(BigDecimal amountTransferred) {
        this.amountTransferred = amountTransferred;
    }

    public BigDecimal getBalanceOfFrom() {
        return balanceOfFrom;
    }

    public void setBalanceOfFrom(BigDecimal balanceOfFrom) {
        this.balanceOfFrom = balanceOfFrom;
    }

    public BigDecimal getBalanceOfTo() {
        return balanceOfTo;
    }

    public void setBalanceOfTo(BigDecimal balanceOfTo) {
        this.balanceOfTo = balanceOfTo;
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
        transfer.setAmountTransferred(result.getBigDecimal("transfers.amount"));
        transfer.setBalanceOfFrom(result.getBigDecimal("accounts.balance"));
        transfer.setBalanceOfTo(result.getBigDecimal("accounts.balance"));
        return transfer;
    }
}
