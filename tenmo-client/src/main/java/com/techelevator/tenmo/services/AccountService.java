package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class AccountService {

    private static final String API_BASE_URL = "http://localhost:8080/account";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getBalance(int userId) {
        String balance = null;
            ResponseEntity<User> response =
                    restTemplate.exchange(API_BASE_URL + userId,
                            HttpMethod.GET, makeAuthEntity(), User.class);

            Integer id = response.getBody().getId();

            //didn't figure out how to route this ID to account to get the balance


        return balance;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

}
