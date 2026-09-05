package com.thiagoLima.ms_autenticacao.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    private String token;
    private String tokenTipo;
    private Long expiraEm;
}
