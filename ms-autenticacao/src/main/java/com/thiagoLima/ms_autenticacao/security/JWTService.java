package com.thiagoLima.ms_autenticacao.security;

import com.thiagoLima.ms_autenticacao.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;import java.nio.charset.StandardCharsets;import java.util.Date;
import java.util.Optional;

@Component
public class JWTService {

    // Chave secreta utilizada pelo JWT para codificar e decodificar o token
    private static final String chavePrivadaJWT = "secretKey-deve-ter-64-caracteres-para-HS512-funcionar-corretamente!!";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(chavePrivadaJWT.getBytes(StandardCharsets.UTF_8));


    /**
     * Metodo para gerar o token JWT
     * @param authentication Autenticação do usuario
     * @return Token.
     */
    public String gerarToken(Authentication authentication)  {

        // 1 dia em miliseconds
        // Varia de acordo com a regra de negocio
        int tempoExpiracao = 866400000;

        // Criando uma data de expiração para o token com base no tempo de expiração
        // Pega a data atual e soma mais 1 dia em milisegundos
        Date dataExpiracao = new Date( new Date().getTime() + tempoExpiracao);

        // Retorna o usuario atual da autenticação
        Usuario usuario = (Usuario) authentication.getPrincipal();


        // Captura todos os dados e retorna um token do JWT
        return Jwts.builder()
                .subject(usuario.getId().toString())
                .issuedAt(new Date())
                .expiration(dataExpiracao)
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Metodo para retornar o id do usuario dono do token
     * @param token do usuário
     * @return id do usuário.
     */
    public Optional<Long> obterIdDoUsuario(String token){
        try {
            // Retorna as permissões do token
            Claims claims = parse(token).getPayload();

            // Retorna o id de dentro do token se entrar, caso contrario retorna null
            return Optional.of(Long.parseLong(claims.getSubject()));
        } catch (Exception e){
            // Se não encontrar nada devolve um optional null.
            return Optional.empty();
        }
    }

    // Metodo que sabe descobrir de dentro do token com base na chave privada qual as
    // permissoes do usuario.
    private Jws<Claims> parse(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token);
    }
}

