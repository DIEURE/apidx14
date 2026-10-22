package com.d2ticom.api.modules.catalog.dto;

import java.math.BigDecimal;

public record ProdutoResponseDTO(
    Long id,
    String codigoBarras,
    String descricao,
    String unidade,
    BigDecimal precoCusto,
    BigDecimal precoVenda,
    BigDecimal estoqueSaldo,
    Boolean ativo
) {}