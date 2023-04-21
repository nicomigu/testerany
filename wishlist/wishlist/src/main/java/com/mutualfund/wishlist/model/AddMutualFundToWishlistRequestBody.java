package com.mutualfund.wishlist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMutualFundToWishlistRequestBody {
	private int customerId;
	private int mfSchemaId;
	private String mfName;
	private String mfFundHouse;
}
