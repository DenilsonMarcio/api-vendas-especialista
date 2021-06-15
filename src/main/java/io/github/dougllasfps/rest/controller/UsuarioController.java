package io.github.dougllasfps.rest.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.dougllasfps.domain.entity.Usuario;
import io.github.dougllasfps.exception.SenhaInvalidaException;
import io.github.dougllasfps.rest.dto.CredenciaisDTO;
import io.github.dougllasfps.rest.dto.TokenDTO;
import io.github.dougllasfps.rest.dto.UsuarioResetaSenhaDTO;
import io.github.dougllasfps.security.jwt.JwtService;
import io.github.dougllasfps.service.impl.UsuarioServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Criar um usuário")
    @ApiResponses({ @ApiResponse(code = 201, message = "Usuário salvo com sucesso"),
                    @ApiResponse(code = 400, message = "Erro de validação")
                })
    public Usuario salvar(@RequestBody @Valid Usuario usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioService.salvar(usuario);
    }

    @PostMapping("/auth")
    @ApiOperation("Autenticar um Usuario")
    @ApiResponses({ @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 400, message = "Erro de validação")
                })
    public TokenDTO autenticar(@RequestBody @Valid CredenciaisDTO credenciais) {
        try {
            Usuario usuario = Usuario.builder().login(credenciais.getLogin()).senha(credenciais.getSenha()).build();
            usuarioService.autenticar(usuario);
            String token = "Bearer " + jwtService.gerarToken(usuario);
            return new TokenDTO(usuario.getLogin(), token);
        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/users")
    @ApiOperation("Listar todos os Usuarios")
    @ApiResponses({ @ApiResponse(code = 200, message = "OK"), 
                    @ApiResponse(code = 401, message = "Não autorizado"),
                    @ApiResponse(code = 403, message = "Token não autenticado")
                })
    public List<Usuario> find() {
        return usuarioService.listaUsuario();
    }

    @PatchMapping("/reset/{id}/{login}")
    @ApiOperation("Resetar senha do Usuario")
    @ApiResponses({ @ApiResponse(code = 200, message = "OK"), 
                    @ApiResponse(code = 401, message = "Não autorizado"),
                    @ApiResponse(code = 403, message = "Token não autenticado")
                 })
    public void resetaSenha(@PathVariable Integer id, @PathVariable String login, @RequestBody @Valid UsuarioResetaSenhaDTO dto) {
        Usuario usuario = usuarioService.loginIsValid(login);
        if (usuario.getId().equals(id)) {
            usuario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
            usuarioService.salvar(usuario);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi possivel resetar a senha");
        }
    }

}
