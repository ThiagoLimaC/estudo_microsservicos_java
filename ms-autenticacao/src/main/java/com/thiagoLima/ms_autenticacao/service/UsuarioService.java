package com.thiagoLima.ms_autenticacao.service;

import com.thiagoLima.ms_autenticacao.dto.LoginRequest;
import com.thiagoLima.ms_autenticacao.dto.LoginResponse;
import com.thiagoLima.ms_autenticacao.dto.TokenResponse;
import com.thiagoLima.ms_autenticacao.dto.UsuarioResponse;
import com.thiagoLima.ms_autenticacao.model.Usuario;
import com.thiagoLima.ms_autenticacao.repository.UsuarioRepository;
import com.thiagoLima.ms_autenticacao.security.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.InputMismatchException;import java.util.List;import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private  final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final ObjectProvider<AuthenticationManager> authenticationManagerProvider;

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_EXPIRATION = 86400L;



    public List<Usuario> obterTodos(){
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obterPorId(Long id){
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> obterPorEmail(String email){
        return usuarioRepository.findByEmail(email);
    }

    public Usuario adicionar(Usuario usuario){
        usuario.setId(null);

        if (obterPorEmail(usuario.getEmail()).isPresent()){
            // Lançar uma exception informando que o usuario ja existe
            throw new InputMismatchException("Já existe um usuário cadastrado com o email " + usuario.getEmail());
        }

        // Codificando a senha para não ficar pública gerando um hash
        String senha = passwordEncoder.encode(usuario.getSenha());

        usuario.setSenha(senha);

        return usuarioRepository.save(usuario);
    }

    @Deprecated
    public LoginResponse logar(String email, String senha){

        try {
            AuthenticationManager authenticationManager = authenticationManagerProvider.getObject();

            Authentication autenticacao = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, senha, Collections.emptyList()));

            String token = BEARER_PREFIX + jwtService.gerarToken(autenticacao);

            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new InputMismatchException("Usuário não encontrado"));

            return new LoginResponse(token, usuario);
        } catch (Exception ex) {
            log.error("Erro ao autenticar usuário com email [{}]", email, ex);
            throw ex;

        }
    }

    /**
     * Realiza o login e retorna APENAS o token JWT.
     * Endpoint: POST /api/autenticacao/login
     *
     * @param request contendo email e senha
     * @return TokenResponse com o JWT
     * @throws Exception se credenciais inválidas ou usuário não encontrado
     */

    public TokenResponse autenticar(LoginRequest request){
        log.info("Iniciando autenticação para email [{}]", request.getEmail());

        try {
            AuthenticationManager authenticationManager = authenticationManagerProvider.getObject();

            // Valida credenciais
            Authentication autenticacao = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getSenha(),
                            Collections.emptyList()
                    )
            );

            // Gerar o token JWT
            String token = jwtService.gerarToken(autenticacao);

            log.info("Autenticação bem-sucedidada para email [{}]", request.getEmail());

            return TokenResponse.builder()
                    .token(token)
                    .tokenTipo("Bearer")
                    .expiraEm(TOKEN_EXPIRATION)
                    .build();
        } catch (Exception ex) {
            log.error("Erro ao autenticar usuário com email [{}]", request.getEmail(), ex);
            throw ex;
        }
    }

    /**
     * Retorna os dados públicos do usuário autenticado.
     * Endpoint: GET /api/autenticacao/perfil
     * Requer autenticação via token JWT.
     *
     * @param usuario autenticado (injetado pelo Spring Security)
     * @return UsuarioResponse com id, email e nome
     */
    public UsuarioResponse obterPerfil(Usuario usuario) {
        log.info("Buscando perfil do usuário [{}]", usuario.getId());

        try {
            // Busca os dados mais atualizados do banco
            Usuario usuarioAtualizado = usuarioRepository.findById(usuario.getId())
                    .orElseThrow(() -> new InputMismatchException("Usuário não encontrado"));

            log.debug("Perfil obtido com sucesso para usuário [{}]", usuario.getId());

            return UsuarioResponse.from(usuarioAtualizado);
        } catch (Exception ex) {
            log.error("Erro ao obter perfil do usuário [{}]", usuario.getId(), ex);
            throw ex;
        }
    }

}
