package com.d2ticom.api.modules.company.dto;

import jakarta.validation.constraints.NotBlank;

public record EmpresaRequestDTO(
    @NotBlank(message = "Razão social é obrigatória") String razaoSocial,
    String nomeFantasia,
    @NotBlank(message = "CNPJ é obrigatório") String cnpj,
    String inscricaoEstadual,
    String endereco,
    String cidade,
    String uf,
    String cep,
    String telefone,
    String email,
    String logoUrl
) {}