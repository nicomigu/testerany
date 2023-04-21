package com.mutualfund.wishlist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MutualFundInWishlistExistenceResponse {
	private boolean doesMutualFundExist;
}
