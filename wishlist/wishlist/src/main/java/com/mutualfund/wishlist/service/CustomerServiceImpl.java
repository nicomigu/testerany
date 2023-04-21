package com.mutualfund.wishlist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mutualfund.wishlist.model.Customer;
import com.mutualfund.wishlist.model.CustomerWishlist;
import com.mutualfund.wishlist.repository.CustomerRepository;

public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public Customer getCustomer(int customerId) throws Exception {
		if(customerId < 0) {
			throw new Exception("Customer Id cannot be less than 0");
		}
		try {
			Customer customer = customerRepository.findByCustomerId(customerId);
			if(customer==null) {
				throw new Exception("Customer not found");
			}
			return customer;
		}
		catch(Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

}
