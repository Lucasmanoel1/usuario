package com.lucasmanoel.usuario.business;


import com.lucasmanoel.usuario.business.converter.Usuarioconverter;
import com.lucasmanoel.usuario.business.dto.UsuarioDTO;
import com.lucasmanoel.usuario.infrastructure.entity.Usuario;
import com.lucasmanoel.usuario.infrastructure.exceptions.ConflictExeception;
import com.lucasmanoel.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.lucasmanoel.usuario.infrastructure.repository.UsuarioRepository;
import com.lucasmanoel.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final Usuarioconverter usuarioconverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioconverter.paraUsuario(usuarioDTO);
        usuarioRepository.save(usuario);
        return usuarioconverter.paraUsuarioDTO(usuario);
    }

    public void emailExiste(String email){
        try {
            if (verificaEmailExistente(email)){
                throw new ConflictExeception("Email já cadastrado");
            }
        } catch (ConflictExeception e) {
            throw new ConflictExeception("Email já cadastrado", e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email){

        return usuarioRepository.existsByEmail(email);
    }
    public Usuario buscaUsuarioFindByEmail(String email){
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado"));
    }
    public void deletaUsuarioPorEmail(String email){
         usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto){
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(()
                -> new ResourceNotFoundException("Email não localizado."));
        Usuario usuario = usuarioconverter.updateUsuario(dto, usuarioEntity);
        return usuarioconverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }
}
