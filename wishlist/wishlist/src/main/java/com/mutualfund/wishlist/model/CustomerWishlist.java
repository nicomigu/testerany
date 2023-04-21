package com.mutualfund.wishlist.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="customer_wishlist")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerWishlist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int wishlistId;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "customerId")
	private Customer customer;
	private String mfName;
	private String mfFundHouse;
	private int mfSchemaId;
}
