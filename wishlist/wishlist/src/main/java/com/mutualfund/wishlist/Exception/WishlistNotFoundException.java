package com.mutualfund.wishlist.Exception;

public class WishlistNotFoundException extends RuntimeException {
	public WishlistNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public WishlistNotFoundException(String message) {
		super(message);
	}

	public WishlistNotFoundException(Throwable cause) {
		super(cause);
	}
}
