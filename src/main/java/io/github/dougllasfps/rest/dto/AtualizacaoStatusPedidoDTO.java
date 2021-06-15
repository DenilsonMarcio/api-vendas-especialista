package io.github.dougllasfps.rest.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizacaoStatusPedidoDTO {

    @NotEmpty(message= "{campo.novoStatus.obrigatorio}")
    private String novoStatus;
}
