package com.mutualfund.wishlist.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mutualfund.wishlist.model.AddMutualFundToWishlistRequestBody;
import com.mutualfund.wishlist.model.CustomerWishlist;
import com.mutualfund.wishlist.model.MutualFundInWishlistExistenceResponse;
import com.mutualfund.wishlist.service.CustomerWishlistService;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/wishlist")
public class WishlistRestController {
	private static final CustomerWishlist CustomerWishlist = null;
	@Autowired
	private CustomerWishlistService customerWishlistService;
	
	@GetMapping("/test")
	public String test() {
		return "Test API is working";
	}
	
	@GetMapping("/get")
	public CustomerWishlist getCustomerWishlist(@RequestHeader(value="Authorization", required = false) String clientToken,@RequestParam int wishlistId) throws Exception {
		return  customerWishlistService.getCustomerWishlist(wishlistId);
	}
	
	@GetMapping("/getbycustomerid")
	public List<CustomerWishlist> getCustomerWishlistByCustomerId(@RequestHeader(value="Authorization", required = false) String clientToken,@RequestParam int customerId) throws Exception {
		//int clientId=customerWishlistService.validateJwtToken(clientToken);
		return  customerWishlistService.getCustomerWishlistByCustomerId(customerId);
	}
	
	@PostMapping("/add")
	public ResponseEntity<CustomerWishlist> addMutualFundToWishList(@RequestHeader(value="Authorization", required = false) String clientToken,@RequestBody AddMutualFundToWishlistRequestBody body) throws Exception{
		//int clientId=customerWishlistService.validateJwtToken(clientToken);
		CustomerWishlist addedMutualFund= customerWishlistService.addMutualFundToWishlist( body.getCustomerId(),body.getMfSchemaId(), body.getMfName(), body.getMfFundHouse());
		return ResponseEntity.ok(addedMutualFund);		
	}
	
	@DeleteMapping("/remove")
	public ResponseEntity removeMutualFundFromWishlist(@RequestHeader(value="Authorization", required = false) String clientToken,@RequestParam int wishlistId) throws Exception {
		customerWishlistService.removeMutualFundFromWishlist(wishlistId);
		return ResponseEntity.ok(null);
	}
	
	@GetMapping("/exists")
	public ResponseEntity<MutualFundInWishlistExistenceResponse> checkIfMutualFundAlreadyExists(@RequestHeader(value="Authorization", required = false) String clientToken,@RequestParam int customerId,@RequestParam int mfSchemaId) throws Exception{
		Optional<CustomerWishlist> customerWishlist=Optional.of(customerWishlistService.getWishlistByCustomerIdAndWishlistId(customerId,mfSchemaId));
		if(customerWishlist.isPresent()) {
			MutualFundInWishlistExistenceResponse response = new MutualFundInWishlistExistenceResponse(true);			
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(null);
	}
}
