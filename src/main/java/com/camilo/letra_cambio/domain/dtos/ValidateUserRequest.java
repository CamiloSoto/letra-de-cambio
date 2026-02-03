package com.camilo.letra_cambio.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateUserRequest {
    private String lastName;
    private String documentType;
    private String documentNumber;

}
