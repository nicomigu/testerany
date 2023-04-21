package com.mutualfund.wishlist.service;

import java.util.List;

import com.mutualfund.wishlist.model.CustomerWishlist;

public interface CustomerWishlistService {
	CustomerWishlist getCustomerWishlist(int wishlistId) throws Exception;
	List<CustomerWishlist> getCustomerWishlistByCustomerId(int customerId) throws Exception;
	CustomerWishlist addMutualFundToWishlist(int customerId, int mfSchemaId, String mfName, String mfFundHouse) throws Exception;
	Void removeMutualFundFromWishlist(int customerId) throws Exception;
	int validateJwtToken(String jwtToken) throws Exception;
	CustomerWishlist getWishlistByCustomerIdAndWishlistId(int customerId,int mfSchemaId) throws Exception;
}
