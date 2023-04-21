package com.mutualfund.wishlist.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.mutualfund.wishlist.Exception.WishlistNotFoundException;
import com.mutualfund.wishlist.model.Customer;
import com.mutualfund.wishlist.model.CustomerWishlist;
import com.mutualfund.wishlist.model.ValidateRequest;
import com.mutualfund.wishlist.model.ValidateResponse;
import com.mutualfund.wishlist.repository.CustomerRepository;
import com.mutualfund.wishlist.repository.CustomerWishlistRepository;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class CustomerWishlistServiceImpl implements CustomerWishlistService {
	@Autowired
	private CustomerWishlistRepository customerWishlistRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Value(value="${auth.api.url}")
	private String AuthUrl;


	@Override
	public CustomerWishlist getCustomerWishlist(int wishlistId) throws Exception {
		if(wishlistId < 0) {
			throw new Exception("Wishlist Id cannot be less than 0");
		}
		try {
			CustomerWishlist customerWishlist = customerWishlistRepository.findByWishlistId(wishlistId);
			if(customerWishlist==null) {
				return null;
			}
			return customerWishlist;
		}
		catch(Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	@Override
	public List<CustomerWishlist> getCustomerWishlistByCustomerId(int customerId) throws Exception {
		if(customerId < 0) {
			throw new Exception("Customer Id cannot be less than 0");
		}
		try {
			List<CustomerWishlist> customerWishlist = customerWishlistRepository.findByCustomerCustomerId(customerId);
			if(customerWishlist.isEmpty()) {
				return null;
			}
			return customerWishlist;
		}
		catch(Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	@Override
	public CustomerWishlist addMutualFundToWishlist(int customerId, int mfSchemaId, String mfName, String mfFundHouse)
			throws Exception {
		if(customerId < 0 || mfSchemaId < 0 ) {
			throw new Exception("Customer Id and MfSchema Id cannot be less than 0");
		}
		if(!StringUtils.isBlank(mfFundHouse)) {
			if(mfFundHouse.length()>64) {
				throw new Exception("mfFundHouse cannot have more than 64 charecters");
			}
		}
		if(!StringUtils.isBlank(mfName)) {
			if(mfName.length()>64) {
				throw new Exception("mfName cannot have more than 64 charecters");
			}
		}
		try {
			Customer customer = customerRepository.findByCustomerId(customerId); 
			Optional<CustomerWishlist> existingMutualFundInWishlist = customerWishlistRepository.findByCustomerCustomerIdAndMfSchemaId(customerId, mfSchemaId);						
			if(existingMutualFundInWishlist.isPresent()) {
				throw new Exception("MutualFund already exists in the wishlist");
			}
			CustomerWishlist customerWishlist= new CustomerWishlist();
			customerWishlist.setCustomer(customer);
			customerWishlist.setMfFundHouse(mfFundHouse);
			customerWishlist.setMfName(mfName);
			customerWishlist.setMfSchemaId(mfSchemaId);			
			return customerWishlistRepository.save(customerWishlist);
		}catch(Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	@Override
	public Void removeMutualFundFromWishlist(int wishlistId) throws Exception {
		if(wishlistId < 0) {
			throw new Exception("WishlistId Id cannot be less than 0");
		}
		try {
			Optional<CustomerWishlist> customerWishlistOptional = Optional.ofNullable(customerWishlistRepository.findByWishlistId(wishlistId));
			if(customerWishlistOptional.isPresent()) {
				CustomerWishlist customerWishlist = customerWishlistOptional.get();
				customerWishlist.setCustomer(null);
				customerWishlistRepository.save(customerWishlist);
				Optional<CustomerWishlist> customerWishlistOptionalAfterRemovingCustomer = Optional.ofNullable(customerWishlistRepository.findByWishlistId(wishlistId));
				if(customerWishlistOptionalAfterRemovingCustomer.isPresent()) {
					customerWishlistRepository.delete(customerWishlistOptionalAfterRemovingCustomer.get());
				}else {
					throw new WishlistNotFoundException("Unable to find wishlist");
				}		
			}else {
				throw new WishlistNotFoundException("Unable to find wishlist");
			}
			
		}catch(Exception ex) {
			throw new Exception(ex.getMessage());
		}
		return null;
	}

	@Override
	public int validateJwtToken(String jwtToken) throws Exception {
		
		try{
			WebClient webClient = WebClient.builder().baseUrl(AuthUrl)
				
				  .defaultHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
				  .build();
			ValidateRequest valReq = new ValidateRequest(jwtToken);
			Mono<ClientResponse> responseEntityMono = webClient.post()
					.uri("/validate")
					.contentType(MediaType.APPLICATION_JSON)
					.body(Mono.just(valReq),ValidateRequest.class)
					.accept(MediaType.APPLICATION_JSON)
					.exchange();
			
			ClientResponse clientResp = responseEntityMono.block();
			
			Mono<ValidateResponse> bodyToMonoResp = clientResp.bodyToMono(ValidateResponse.class);
			ValidateResponse responseValue = bodyToMonoResp.block();
			if(responseValue.getId()!=0) {
				return responseValue.getId();
			}			
		}
		catch(Exception ex) {
			throw new Exception("Unable to validate the token "+ex.getMessage());
		}
		return 0;
	}

	@Override
	public CustomerWishlist getWishlistByCustomerIdAndWishlistId(int customerId, int mfSchemaId) throws Exception {
		if(customerId < 0 || mfSchemaId < 0 ) {
			throw new Exception("Customer Id and MfSchema Id cannot be less than 0");
		}
		try {
			Optional<CustomerWishlist> customerWishlist = customerWishlistRepository.findByCustomerCustomerIdAndMfSchemaId(customerId, mfSchemaId);
			if(customerWishlist.isPresent()) {
				return customerWishlist.get();
			}
			else {
				throw new WishlistNotFoundException("Unable to find mutualfund in wishlist") ;
			}			
		}catch(Exception ex) {
			throw new WishlistNotFoundException("Unable to find mutualfund in wishlist");
		}
	}

}
