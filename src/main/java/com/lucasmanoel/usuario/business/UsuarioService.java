package com.lucasmanoel.usuario.business;


import com.lucasmanoel.usuario.business.converter.Usuarioconverter;
import com.lucasmanoel.usuario.business.dto.EnderecoDTO;
import com.lucasmanoel.usuario.business.dto.TelefoneDTO;
import com.lucasmanoel.usuario.business.dto.UsuarioDTO;
import com.lucasmanoel.usuario.infrastructure.entity.Endereco;
import com.lucasmanoel.usuario.infrastructure.entity.Telefone;
import com.lucasmanoel.usuario.infrastructure.entity.Usuario;
import com.lucasmanoel.usuario.infrastructure.exceptions.ConflictExeception;
import com.lucasmanoel.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.lucasmanoel.usuario.infrastructure.repository.EnderecoRepository;
import com.lucasmanoel.usuario.infrastructure.repository.TelefoneRepository;
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
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

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
    public UsuarioDTO buscaUsuarioFindByEmail(String email){
        //Sim, o try/catch está redundante pois já estou fazendo o tratamento do erro.
        try {
            return usuarioconverter.paraUsuarioDTO(usuarioRepository
                    .findByEmail(email)
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Email não encontrado")));
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Email não encontrado");
        }
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

    public EnderecoDTO atualizaEndereco (Long idEndereco, EnderecoDTO enderecoDTO){
        Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(
                () -> new ResourceNotFoundException("Id não localizado."));
        Endereco endereco = usuarioconverter.updateEndereco(enderecoDTO, entity);
        return usuarioconverter.paraEnderecoDTO(enderecoRepository.save(entity));
    }

    public TelefoneDTO atualizaTelefone (Long idTelefone, TelefoneDTO telefoneDTO){
        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(
                () -> new ResourceNotFoundException("Id não encontrado.")
        );
        Telefone telefone = usuarioconverter.updateTelefone(telefoneDTO, entity);
        return usuarioconverter.paraTelefoneDTO(telefoneRepository.save(entity));
    }

    public  EnderecoDTO cadastroEndereco(String token, EnderecoDTO dto){
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email não encontrado" + email));
        Endereco endereco = usuarioconverter.paraEnderecoEntity(dto, usuario.getId());
        return usuarioconverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO cadastroTelefone(String token, TelefoneDTO dto){

        String email = jwtUtil.extrairEmailToken(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email não localizado."));
        Telefone telefone = usuarioconverter.paraTelefoneEntity(dto, usuario.getId());
        return usuarioconverter.paraTelefoneDTO(telefoneRepository.save(telefone));

    }
}
