package com.teste.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestDto {
    private String marca;
    private String modelo;
    private Integer ano;
    private String cor;
}
