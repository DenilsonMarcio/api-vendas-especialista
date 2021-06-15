package io.github.dougllasfps.rest.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioResetaSenhaDTO {

    @NotEmpty(message = "{campo.novaSenha.obrigatorio}")
    private String novaSenha;
}
