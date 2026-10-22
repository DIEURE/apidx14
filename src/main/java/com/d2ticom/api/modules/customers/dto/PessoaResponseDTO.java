package com.d2ticom.api.modules.customers.dto;

import com.d2ticom.api.modules.customers.domain.TipoPessoa;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PessoaResponseDTO(
    Long id,
    TipoPessoa tipo,
    String nome,
    String nomeFantasia,
    String documento,
    String inscricaoEstadual,
    String rg,
    LocalDate dataNascimento,
    String telefone,
    String email,
    String endereco,
    String cidade,
    String uf,
    String cep,
    Boolean ativo,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}