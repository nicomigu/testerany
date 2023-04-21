package com.mutual_fund.Exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomException extends RuntimeException{

    public CustomException(String msg) {
        super(msg);
    }
}
