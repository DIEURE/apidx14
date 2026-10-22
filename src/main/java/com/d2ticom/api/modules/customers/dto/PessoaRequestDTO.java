package com.d2ticom.api.modules.customers.dto;

import com.d2ticom.api.modules.customers.domain.TipoPessoa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PessoaRequestDTO(
    @NotNull TipoPessoa tipo,
    @NotBlank @Size(max = 200) String nome,
    @Size(max = 200) String nomeFantasia,
    @NotBlank @Size(max = 20) String documento,
    @Size(max = 20) String inscricaoEstadual,
    @Size(max = 20) String rg,
    LocalDate dataNascimento,
    @Size(max = 20) String telefone,
    @Size(max = 150) String email,
    @Size(max = 200) String endereco,
    @Size(max = 100) String cidade,
    @Size(max = 2) String uf,
    @Size(max = 9) String cep,
    Boolean ativo
) {}