package com.mutual_fund.model;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenRespond {
    private Long id;
    private String email;
}
