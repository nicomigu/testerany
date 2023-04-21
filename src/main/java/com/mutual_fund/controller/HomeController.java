package com.mutual_fund.controller;

import com.mutual_fund.CustomerReponse.ErrorResponse;
import com.mutual_fund.CustomerReponse.SuccessResponse;
import com.mutual_fund.entities.Customer;
import com.mutual_fund.model.JwtRequest;
import com.mutual_fund.model.JwtResponse;
import com.mutual_fund.model.TokenRequest;
import com.mutual_fund.model.TokenRespond;
import com.mutual_fund.repository.CustomerRepository;
import com.mutual_fund.service.UserService;
import com.mutual_fund.utility.JWTUtility;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "homemade";
    }

//    private final AuthenticationService service;

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    final private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerRepository customerRepository;

//    @PostMapping("/authenticate")
//    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception{
//
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            jwtRequest.getEmail(),
//                            jwtRequest.getPassword()
//                    )
//            );
//        } catch (BadCredentialsException e) {
////            System.out.println("INVALID_CREDENTIALS");
//            throw new Exception("INVALID_CREDENTIALS", e);
//        }
//
//        final UserDetails userDetails
//                = userService.loadUserByUsername(jwtRequest.getEmail());
//
//        final String token =
//                jwtUtility.generateToken(userDetails);
//
//        return new JwtResponse(token);
//    }


    @PostMapping("/authenticateV2")
    public ResponseEntity<?> authenticate2(@RequestBody JwtRequest jwtRequest){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getEmail(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(ErrorResponse.builder()
                    .message("INVALID_CREDENTIALS" + e.getMessage())
                    .code(HttpStatus.UNAUTHORIZED.value()).build(), HttpStatus.UNAUTHORIZED);
        };

        final UserDetails userDetails
                = userService.loadUserByUsername(jwtRequest.getEmail());

        final String token =
                jwtUtility.generateToken(userDetails);

        return new ResponseEntity<>(
                JwtResponse.builder()
                        .token(token)
                        .id(customerRepository.findByEmail(userDetails.getUsername()).get().getId()).build(),
                HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        Customer savedCustomer = null;
        ResponseEntity response = null;
        try {
            String hashPwd = passwordEncoder.encode(customer.getPwd());
//            System.out.println(hashPwd);
            customer.setPwd(hashPwd);
            Optional<Customer> customer1 = customerRepository.findByEmail(customer.getEmail());
            if (customer1.isPresent()) {
                response = ResponseEntity
                        .status(HttpStatus.OK)
                        .body("User email already exist");
                return response;
            }

            System.out.println(customer);
            savedCustomer = customerRepository.save(customer);
            if (savedCustomer.getId() > 0) {
                response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body("Given user details are successfully registered");
            }
        } catch (Exception ex) {
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An exception occurred due to " + ex.getMessage());
        }
        return response;
    }

    @PostMapping("/registerV2")
    public ResponseEntity<?> registerUser2(@RequestBody Customer customer) {

        ErrorResponse errorResponse = new ErrorResponse();
        SuccessResponse registerSuccessResponse = null;

        try {
            String hashPwd = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPwd);

            Optional<Customer> optCustomer = customerRepository.findByEmail(customer.getEmail());
            if (optCustomer.isPresent()) {
                errorResponse.setMessage("User email already exist");
                errorResponse.setCode(HttpStatus.OK.value());

                return new ResponseEntity<>(errorResponse, HttpStatus.OK);
            }

            Customer savedCustomer = customerRepository.save(customer);
            if (savedCustomer.getId() > 0) {
                registerSuccessResponse = new SuccessResponse();
                registerSuccessResponse.setMessage("Given user details are successfully registered");
            }
        } catch (Exception ex) {
            errorResponse.setMessage("An exception occurred due to " + ex.getMessage());
            errorResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(registerSuccessResponse, HttpStatus.CREATED);
    }


    @GetMapping("/validateToken")
    public ResponseEntity<?> validate(@RequestBody TokenRequest tokenRequest) {
        //Todo
        String email = jwtUtility.getUsernameFromToken(tokenRequest.getToken());

        Optional<Customer> customer = customerRepository.findByEmail(email);
        Customer customer1 = customerRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));
        TokenRespond tokenRespond = TokenRespond.builder()
                .email(customer1.getEmail())
                .id(customer1.getId())
                .build();
//
        return ResponseEntity.status(HttpStatus.OK)
                .body(tokenRespond);
    }


    @GetMapping("/validateTokenV2")
    public ResponseEntity<?> validate2(@RequestBody TokenRequest tokenRequest) {

        SuccessResponse registerSuccessResponse = null;
        String email;
        try {
            email = jwtUtility.getUsernameFromToken(tokenRequest.getToken());

            UserDetails userDetails
                    = userService.loadUserByUsername(email);

            jwtUtility.validateToken(tokenRequest.getToken(), userDetails);

        } catch (MalformedJwtException e) {
            return new ResponseEntity<>(
                    ErrorResponse.builder().message(e.getMessage()).code(HttpStatus.UNAUTHORIZED.value()).build(),HttpStatus.UNAUTHORIZED) ;
        } catch (Exception e) {

            return new ResponseEntity<>(ErrorResponse.builder().message(e.getMessage()).code(HttpStatus.UNAUTHORIZED.value()).build(), HttpStatus.UNAUTHORIZED);

        };


        Optional<Customer> optCustomer = customerRepository.findByEmail(email);
        Customer customer = optCustomer.orElseThrow(() -> new RuntimeException("user not found"));

        TokenRespond tokenRespond = TokenRespond.builder()
                .email(customer.getEmail())
                .id(customer.getId())
                .build();
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(tokenRespond);

        return new ResponseEntity<>(
                TokenRespond.builder()
                .email(customer.getEmail())
                .id(customer.getId())
                .build(), HttpStatus.OK);
    }

//    private ResponseEntity<TokenRespond> getTokenRespondResponseEntity(TokenRequest tokenRequest) {
//
//
//        String email = jwtUtility.getUsernameFromToken(tokenRequest.getToken());
//
//        UserDetails userDetails
//                = userService.loadUserByUsername(email);
//
//        jwtUtility.validateToken(token,userDetails)
//    }

}
