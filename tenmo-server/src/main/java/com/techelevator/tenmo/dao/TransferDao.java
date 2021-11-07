package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> getTransfersByUsername(String username);

    Transfer getTransferByTransferId(int transferId);

    Transfer sendTransfer (String chosenUsername, BigDecimal amount, String currentUsername);


}
