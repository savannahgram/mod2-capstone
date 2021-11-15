package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.SendDTO;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class TransferService {
    private static final String API_BASE_URL = "http://localhost:8080/transfers";
    private final RestTemplate restTemplate = new RestTemplate();


    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public TransferService (){

    }

    public Transfer[] getTransfersByUsername(String username, AuthenticatedUser currentUser) {
        Transfer[] transfers = null;

        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(API_BASE_URL + "/username/" + username,
                            HttpMethod.GET, makeAuthEntity(currentUser), Transfer[].class);
            transfers = response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }

        return transfers;
    }

    public Transfer getTransferByTransferId(AuthenticatedUser currentUser, int transferId) {
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(API_BASE_URL + "/transferid/" + transferId,
                            HttpMethod.GET, makeAuthEntity(currentUser), Transfer.class);
transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return transfer;
    }

    public Transfer sendTransfer(AuthenticatedUser currentUser, SendDTO sendDTO){
        HttpEntity<SendDTO> entity = makeSendDTOEntity(currentUser, sendDTO);
        Transfer newTransfer = null;
        try {
            newTransfer =
                    restTemplate.exchange(API_BASE_URL + "/send",
                            HttpMethod.POST, entity, Transfer.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return newTransfer;
    }
/*
    public Transfer sendTransfer(AuthenticatedUser currentUser, SendDTO sendDTO, String currentUsername){
        HttpEntity<SendDTO> entity = makeSendDTOEntity(currentUser, sendDTO);
        Transfer newTransfer = null;
        try {
            newTransfer =
                    restTemplate.exchange(API_BASE_URL + "/send",
                            HttpMethod.POST, entity, Transfer.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return newTransfer;
    }


 */
    private HttpEntity<SendDTO> makeSendDTOEntity(AuthenticatedUser currentUser, SendDTO sendDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<SendDTO>(sendDTO, headers);
    }


    private HttpEntity<Void> makeAuthEntity(AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }

}
