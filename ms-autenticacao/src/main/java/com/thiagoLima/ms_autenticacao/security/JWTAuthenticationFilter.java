package com.thiagoLima.ms_autenticacao.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.ObjectProvider;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final ObjectProvider<CustomUserDetailsService> customUserDetailsServiceProvider;

    // Metodo principal onde toda a requisição bate antes de chegar no nosso endpoint.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info(" JWTAuthenticationFilter invocado - Método: {}, URI: {}",
                request.getMethod(), request.getRequestURI());

        try {
            // Pego o token de dentro da requisição
            String token = obterToken(request);
            log.debug("Token extraído: {}", token != null ? "SIM ✓" : "NÃO ✗");

            // Se não tem token, segue sem autenticar
            if (token == null){
                log.debug("⏭  Nenhum token encontrado, seguindo requisição SEM autenticar");
                filterChain.doFilter(request, response);
                return;
            }

            // Pego o id do usuário que está dentro do token
            Optional<Long> id = jwtService.obterIdDoUsuario(token);
            log.debug(" ID extraído do token: {}", id.isPresent() ? id.get() : "NÃO ENCONTRADO");

            // Se o token é inválido, segue sem autenticar
            if (!id.isPresent()) {
                log.warn(" Token inválido ou expirado - seguindo SEM autenticar");
                filterChain.doFilter(request, response);
                return;
            }

            // Pego o usuario dono do token pelo seu Id
            CustomUserDetailsService customUserDetailsService = customUserDetailsServiceProvider.getObject();

            UserDetails usuario = customUserDetailsService.obterUsarioPorId(id.get());
            log.info("  Usuário encontrado no banco: {} | Authorities: {}",
                    usuario.getUsername(), usuario.getAuthorities());



            // Nesse ponto verificamos se o usuário está autenticado ou não
            // Aqui também poderíamos validar as permissões
            UsernamePasswordAuthenticationToken autenticacao =
                    new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

            // Mudando a autenticação para a própria requisição
            autenticacao.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Repasso a autenticação para o contexto do security.
            // A partir de agora o spring toma conta de tudo para mim
            SecurityContextHolder.getContext().setAuthentication(autenticacao);
        }

        catch (UsernameNotFoundException e){
            log.warn("Usuário não encontrado ao processar JWT", e);
        }
        catch (Exception e){
            log.error("Erro ao processar autenticação JWT", e);
        }

        // Libera a requisição para o controller
        filterChain.doFilter(request, response);
    }

    private String obterToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        log.debug("Header Authorization recebido: {}", token != null ? "SIM ✓" : "NÃO ✗");
        // Verifica se veio alguma coisa sem ser espaços em branco dentro do token
        if(!StringUtils.hasText(token)){
            log.debug("Header Authorization vazio ou não encontrado");
            return null;
        }

        log.debug("Token extraído com sucesso (tamanho: {} caracteres)", token.length());
        return token.substring(7);
    }
}
