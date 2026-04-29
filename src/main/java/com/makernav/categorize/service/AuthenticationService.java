package com.makernav.categorize.service;

import com.makernav.categorize.dto.UsuarioRequestDTO;
import com.makernav.categorize.dto.UsuarioResponseDTO;
import com.makernav.categorize.dto.mapper.UsuarioMapper;
import com.makernav.categorize.infra.repository.UsuarioRepository;
import com.makernav.categorize.model.Cargo;
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
    public UsuarioResponseDTO createUsuario(UsuarioRequestDTO usuarioRequestDTO) {
        var usuario = usuarioMapper.toEntity(usuarioRequestDTO);
        usuario.setSenha(passwordEncoder.encode(usuarioRequestDTO.senha()));
        usuario.setCargo(Cargo.PROFESSOR);

        var usuarioSalvo = usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDTO(usuarioSalvo);
    }
}
