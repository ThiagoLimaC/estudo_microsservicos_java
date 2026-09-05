package com.thiagoLima.ms_autenticacao.security;

import com.thiagoLima.ms_autenticacao.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String email) {
//        Usuario usuario = getUser(() -> usuarioService.obterPorEmail(email));
//        return usuario;
        return usuarioService.obterPorEmail(email).get();
    }

    public UserDetails obterUsarioPorId(Long id){
        return usuarioService.obterPorId(id).get();
    }

//    private Usuario getUser(Supplier<Optional<Usuario>> supplier){
//        return supplier.get().orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
//    }
}
