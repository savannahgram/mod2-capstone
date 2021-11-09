package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountService {

    private static final String API_BASE_URL = "http://localhost:8080/account/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public AccountService (){

    }

    public BigDecimal[] showBalance(AuthenticatedUser currentUser) {
        BigDecimal[] balances = new BigDecimal [1];

        try {
            Account account =
                    restTemplate.exchange(API_BASE_URL + "user/" + currentUser.getUser().getUsername(),
                            HttpMethod.GET, makeAuthEntity(currentUser), Account.class).getBody();
           balances[0] = account.getBalance();
//can get account array instead for multiple results

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }

        return balances;
    }



    private HttpEntity<Void> makeAuthEntity(AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }

}
