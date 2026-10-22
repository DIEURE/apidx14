package com.d2ticom.api.modules.sales.dto;

import java.math.BigDecimal;

public record ItemPedidoDTO(
    Long produtoId,
    String descricaoProduto,
    String unidade,
    BigDecimal quantidade,
    BigDecimal precoUnitario,
    BigDecimal subtotal
) {}
