package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class UserService {
    private static final String API_BASE_URL = "http://localhost:8080/user/";
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public User[] findAll(){
        User[] users = null;
        try {
            users =
                    restTemplate.exchange(API_BASE_URL + "all/",
                            HttpMethod.GET, makeAuthEntity(), User[].class).getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public User[] findByUsername(){
        User[] users = null;
        try {
            users =
                    restTemplate.exchange(API_BASE_URL + "byusername/",
                            HttpMethod.GET, makeAuthEntity(), User[].class).getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public User[] findIdByUsername(){
        User[] users = null;
        try {
            users =
                    restTemplate.exchange(API_BASE_URL + "findid/",
                            HttpMethod.GET, makeAuthEntity(), User[].class).getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }



    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }
}
