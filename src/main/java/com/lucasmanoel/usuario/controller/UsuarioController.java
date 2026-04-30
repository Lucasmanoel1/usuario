package com.lucasmanoel.usuario.controller;

import com.lucasmanoel.usuario.business.UsuarioService;
import com.lucasmanoel.usuario.business.ViaCepService;
import com.lucasmanoel.usuario.business.dto.EnderecoDTO;
import com.lucasmanoel.usuario.business.dto.TelefoneDTO;
import com.lucasmanoel.usuario.business.dto.UsuarioDTO;
import com.lucasmanoel.usuario.infrastructure.clients.ViaCepDTO;
import com.lucasmanoel.usuario.infrastructure.security.JwtUtil;
import com.lucasmanoel.usuario.infrastructure.security.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Tag(name = "Tarefas", description = "Cadastro Tarefas dos usuários")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ViaCepService viaCepService;


    @PostMapping
    @Operation(summary = "Cria usuario",description = "Cria um novo usuario")
    @ApiResponse(responseCode = "200", description = "Usuario criado com sucesso")
    @ApiResponse(responseCode = "403", description = "Dados inválidos para criação do usuario")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody  UsuarioDTO usuarioDTO){
        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));
    }
    @PostMapping("/login")
    @Operation(summary = "Login",description = "Realiza login no servidor")
    @ApiResponse(responseCode = "200", description = "Login feito com sucesso")
    @ApiResponse(responseCode = "403", description = "Dados inválidos")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado")
    public ResponseEntity<String> login(@RequestBody UsuarioDTO usuarioDTO){
        return ResponseEntity.ok(usuarioService.autenticarUsuario(usuarioDTO));
    }
    @GetMapping
    @Operation(summary = "Busca pelo email",description = "Busca usuario pelo email no servidor")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado com sucesso")
    @ApiResponse(responseCode = "403", description = "Dados inválidos")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    @ApiResponse(responseCode = "401", description = "Usuário não localizado")
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(usuarioService.buscaUsuarioFindByEmail(email));
    }
    @DeleteMapping("/{email}")
    @Operation(summary = "Deleta usuario",description = "Deleta usuario por email")
    @ApiResponse(responseCode = "200", description = "Usuario deletado com sucesso")
    @ApiResponse(responseCode = "403", description = "Dados inválidos")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable String email){
        usuarioService.deletaUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }
    @PutMapping
    @Operation(summary = "Altera dados",description = "Altera dados do usuario")
    @ApiResponse(responseCode = "200", description = "Usuario alterado com sucesso")
    @ApiResponse(responseCode = "403", description = "Dados inválidos para alteração do usuario")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado")
    public ResponseEntity<UsuarioDTO> alteraDadosUsuario(@RequestBody UsuarioDTO dto, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.atualizaDadosUsuario(token, dto));
    }
    @PutMapping("/endereco")
    @Operation(summary = "Altera endereço",description = "Altera endereço do usuario")
    @ApiResponse(responseCode = "200", description = "Endereco alterado com sucesso")
    @ApiResponse(responseCode = "403", description = "Dados inválidos")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado")
    public ResponseEntity<EnderecoDTO> alteraEndereco(@RequestBody EnderecoDTO enderecoDTO,
                                                      @RequestParam("id") Long id){
        return ResponseEntity.ok(usuarioService.atualizaEndereco(id, enderecoDTO));
    }
    @PutMapping("/telefone")
    @Operation(summary = "Altera telefone",description = "Altera telefone do usuario")
    @ApiResponse(responseCode = "200", description = "Telefone alterado com sucesso")
    @ApiResponse(responseCode = "403", description = "Dados inválidos")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado")
    public ResponseEntity<TelefoneDTO> alteraTelefone(@RequestBody TelefoneDTO telefoneDTO,
                                                      @RequestParam("id") Long id){
        return ResponseEntity.ok(usuarioService.atualizaTelefone(id, telefoneDTO));
    }
    @PostMapping("/telefone")
    @Operation(summary = "Cadastra telefone",description = "Cadastra telefone do usuario")
    @ApiResponse(responseCode = "200", description = "Telefone cadastrado com sucesso")
    @ApiResponse(responseCode = "403", description = "Dados inválidos")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado")
    public ResponseEntity<TelefoneDTO> cadastraTelefone(@RequestBody TelefoneDTO telefoneDTO,
                                                        @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.cadastroTelefone(token, telefoneDTO));
    }
    @PostMapping("/endereco")
    @Operation(summary = "Cadastra endereço",description = "Adiciona mais endereços do usuario")
    @ApiResponse(responseCode = "200", description = "Endereco adicionado com sucesso")
    @ApiResponse(responseCode = "403", description = "Dados inválidos")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado")
    public ResponseEntity<EnderecoDTO> cadastraEndereco(@RequestBody EnderecoDTO dto,
                                                         @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.cadastroEndereco(token, dto));
    }

    @GetMapping("/endereco/{cep}")
    @Operation(summary = "Busca Cep",description = "Consulta os dados de endereço completos a partir de um CEP de 8 dígitos.")
    @ApiResponse(responseCode = "200", description = "Endereço encontrado")
    @ApiResponse(responseCode = "400", description = "CEP com formato inválido")
    @ApiResponse(responseCode = "404", description = "CEP não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    public ResponseEntity<ViaCepDTO> buscarDadosCep(@PathVariable("cep") String cep){
        return ResponseEntity.ok(viaCepService.buscaDadosEndereco(cep));
    }
}
