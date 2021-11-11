package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class UserService {
    private static final String API_BASE_URL = "http://localhost:8080/user";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public UserService (){

    }

    public User[] findAll(AuthenticatedUser currentUser){
        User[] users = null;
        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(API_BASE_URL + "/all",
                            HttpMethod.GET, makeAuthEntity(currentUser), User[].class);
            users = response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public User[] findByUsername(AuthenticatedUser currentUser){
        User[] users = null;
        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(API_BASE_URL + "/byusername",
                            HttpMethod.GET, makeAuthEntity(currentUser), User[].class);
            users = response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public User findById(AuthenticatedUser currentUser, int id){
        User user = null;
        try {
            user =
                    restTemplate.exchange(API_BASE_URL + "/" + id,
                            HttpMethod.GET, makeAuthEntity(currentUser), User.class).getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    public User[] findIdByUsername(AuthenticatedUser currentUser, String username){
        User[] users = null;
        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(API_BASE_URL + "/" + "findid",
                            HttpMethod.GET, makeAuthEntity(currentUser), User[].class);
            users = response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public User findUserByAccountId(AuthenticatedUser currentUser, int accountId){
        User user = null;
        try {
            user =
                    restTemplate.exchange(API_BASE_URL + "/findbyaccount",
                            HttpMethod.GET, makeAuthEntity(currentUser), User.class).getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }



    private HttpEntity<Void> makeAuthEntity(AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }
}
