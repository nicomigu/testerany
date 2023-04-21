package com.mutualfund.wishlist.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateRequest {

	@NotBlank(message="Token should not be blank")
	private String jwt;
	
}