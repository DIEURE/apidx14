package com.d2ticom.api.modules.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record ProdutoRequestDTO(
    String codigoBarras,
    @NotBlank(message = "Descrição é obrigatória") String descricao,
    String unidade,
    @PositiveOrZero BigDecimal precoCusto,
    @NotNull(message = "Preço de venda é obrigatório") @PositiveOrZero BigDecimal precoVenda,
    @PositiveOrZero BigDecimal estoqueSaldo,
    Boolean ativo
) {}