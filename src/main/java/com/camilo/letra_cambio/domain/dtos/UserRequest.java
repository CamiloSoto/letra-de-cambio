package com.camilo.letra_cambio.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String nombre;
    private String documento;
    private String documentoCiudad;
}
