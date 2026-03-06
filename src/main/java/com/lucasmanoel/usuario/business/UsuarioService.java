package com.lucasmanoel.usuario.business;


import com.lucasmanoel.usuario.business.converter.Usuarioconverter;
import com.lucasmanoel.usuario.business.dto.UsuarioDTO;
import com.lucasmanoel.usuario.infrastructure.entity.Usuario;
import com.lucasmanoel.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final Usuarioconverter usuarioconverter;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioconverter.paraUsuario(usuarioDTO);
        usuarioRepository.save(usuario);
        return usuarioconverter.paraUsuarioDTO(usuario);
    }
}
