package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class SendDTO {
    private String chosenUsername;
    private BigDecimal amount;

    public String getChosenUsername() {
        return chosenUsername;
    }

    public void setChosenUsername(String chosenUsername) {
        this.chosenUsername = chosenUsername;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
