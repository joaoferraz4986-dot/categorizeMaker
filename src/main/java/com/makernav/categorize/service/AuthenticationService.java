package com.makernav.categorize.service;

import com.makernav.categorize.dto.UsuarioDTO;
import com.makernav.categorize.dto.mapper.UsuarioMapper;
import com.makernav.categorize.infra.repository.UsuarioRepository;
import com.makernav.categorize.model.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private final UsuarioMapper usuarioMapper;

    public AuthenticationService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username);
    }

    @Transactional
    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
        var passwordEncrypted = passwordEncoder.encode(usuarioDTO.senha());

        var usuario = new Usuario();
        usuario.setNome(usuarioDTO.nome());
        usuario.setEmail(usuarioDTO.email());
        usuario.setSenha(passwordEncrypted);

        var usuarioSalvo = usuarioRepository.save(usuario);

        return usuarioMapper.toDTO(usuarioSalvo);
    }
}
