package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.SendDTO;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequestMapping("/transfers")
@RestController
public class TransferController {
    private TransferDao transferDao;

    public TransferController (TransferDao transferDao){
        this.transferDao = transferDao;
    }

    //path variable?
    @RequestMapping(path = "/username/{username}", method = RequestMethod.GET)
    public Transfer[] getTransfersByUsername(@PathVariable String username){
        return transferDao.getTransfersByUsername(username);
    }

    @RequestMapping(path = "/transferid/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferByTransferId(@PathVariable int transferId){
        return transferDao.getTransferByTransferId(transferId);
    }

    @RequestMapping(path = "/send", method = RequestMethod.POST)
    Transfer sendTransfer (@RequestBody SendDTO sendDTO, Principal currentUser){
        return transferDao.sendTransfer(sendDTO.getChosenUsername(), sendDTO.getAmount(), currentUser.getName());
    }





}
