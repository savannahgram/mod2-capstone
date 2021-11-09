package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class TransferService {
    private static final String API_BASE_URL = "http://localhost:8080/transfer/";
    private final RestTemplate restTemplate = new RestTemplate();


    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public TransferService (){

    }

    public Transfer[] getTransfersByUsername(AuthenticatedUser currentUser, String username) {
        Transfer[] transfers = null;

        try {
            transfers =
                    restTemplate.exchange(API_BASE_URL + "username/" + username,
                            HttpMethod.GET, makeAuthEntity(currentUser), Transfer[].class).getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }

        return transfers;
    }

    public Transfer getTransferByTransferId(AuthenticatedUser currentUser, int transferId) {
        Transfer transfer = null;
        try {
            transfer =
                    restTemplate.exchange(API_BASE_URL + "transferid/" + transferId,
                            HttpMethod.GET, makeAuthEntity(currentUser), Transfer.class).getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return transfer;
    }

    public Transfer sendTransfer(AuthenticatedUser currentUser, String chosenUsername, BigDecimal amount, String currentUsername){
        Transfer newTransfer = null;
        try {
            newTransfer =
                    restTemplate.exchange(API_BASE_URL + "send",
                            HttpMethod.POST, makeAuthEntity(currentUser), Transfer.class).getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return newTransfer;
    }

    private HttpEntity<Void> makeAuthEntity(AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }

}
