package com.d2ticom.api.modules.company.dto;

public record EmpresaResponseDTO(
    Long id,
    String razaoSocial,
    String nomeFantasia,
    String cnpj,
    String inscricaoEstadual,
    String endereco,
    String cidade,
    String uf,
    String cep,
    String telefone,
    String email,
    String logoUrl
) {}