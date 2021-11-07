package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequestMapping("/account/transfer/")
@RestController
public class TransferController {
    private TransferDao transferDao;

    //path variable?
    @RequestMapping(path = "username/", method = RequestMethod.GET)
    public List<Transfer> getTransfersByUsername(Principal currentUser){
        return transferDao.getTransfersByUsername(currentUser.getName());
    }

    @RequestMapping(path = "id/", method = RequestMethod.GET)
    public Transfer getTransfersByTransferId(int transferId){
        return transferDao.getTransfersByTransferId(transferId);
    }

    @RequestMapping(path = "send/", method = RequestMethod.POST)
    Transfer sendTransfer (String chosenUsername, BigDecimal amount, Principal currentUser){
        return transferDao.sendTransfer(chosenUsername, amount, currentUser.getName());
    }





}
