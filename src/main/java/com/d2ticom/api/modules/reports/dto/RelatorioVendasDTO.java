package com.d2ticom.api.modules.reports.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record RelatorioVendasDTO(
    LocalDate dataInicio,
    LocalDate dataFim,
    Long totalVendas,
    BigDecimal totalBruto,
    BigDecimal totalDesconto,
    BigDecimal totalLiquido,
    BigDecimal ticketMedio,
    Map<String, Long> quantidadePorFormaPagamento,
    Map<String, BigDecimal> valorPorFormaPagamento
) {}