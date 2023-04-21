package com.mutualfund.wishlist.service;

import com.mutualfund.wishlist.model.Customer;

public interface CustomerService {
	Customer getCustomer(int customerId) throws Exception;
}	
