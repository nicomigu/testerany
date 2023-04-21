package com.mutualfund.wishlist.rest;

import static org.mockito.ArgumentMatchers.intThat;

import java.util.Arrays;

public class TestArray {

	public static void main(String[] args) {
		int[] inputArray={1,2,6,9,12,13};
		Arrays.sort(inputArray);
		int secondLargestNumber =  inputArray[inputArray.length-2];
		System.out.println("Second largest integer from given array = "+secondLargestNumber);
	}
}
