package com.d2ticom.api.modules.sales.dto;

import java.math.BigDecimal;

public record FinalizacaoRequestDTO(
    BigDecimal valorDesconto,
    String senhaAutorizacao
) {}