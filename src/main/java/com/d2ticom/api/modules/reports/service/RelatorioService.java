package com.d2ticom.api.modules.reports.service;

import com.d2ticom.api.modules.reports.dto.RelatorioVendasDTO;
import com.d2ticom.api.modules.sales.domain.Pedido;
import com.d2ticom.api.modules.sales.domain.StatusPedido;
import com.d2ticom.api.modules.sales.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioService {

    private final PedidoRepository pedidoRepository;

    public RelatorioService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public RelatorioVendasDTO gerarRelatorio(LocalDate inicio, LocalDate fim) {
        List<Pedido> pedidos = pedidoRepository.findByStatusAndCreatedAtBetween(
            StatusPedido.FINALIZADO,
            inicio.atStartOfDay(),
            fim.atTime(LocalTime.MAX)
        );

        BigDecimal totalVendido = BigDecimal.ZERO;
        BigDecimal desconto = BigDecimal.ZERO;
        Map<String, Long> qtdPorForma = new LinkedHashMap<>();
        Map<String, BigDecimal> valorPorForma = new LinkedHashMap<>();

        for (Pedido p : pedidos) {
            BigDecimal vTotal = p.getValorTotal() != null ? p.getValorTotal() : BigDecimal.ZERO;
            BigDecimal vDesc = p.getValorDesconto() != null ? p.getValorDesconto() : BigDecimal.ZERO;

            totalVendido = totalVendido.add(vTotal);
            desconto = desconto.add(vDesc);

            String forma = p.getFormaPagamento() != null ? p.getFormaPagamento().name() : "OUTRO";
            qtdPorForma.merge(forma, 1L, Long::sum);
            valorPorForma.merge(forma, vTotal, BigDecimal::add);
        }

        // valorTotal no banco já está LÍQUIDO. Bruto real = totalVendido + desconto.
        BigDecimal liquido = totalVendido;
        BigDecimal bruto = totalVendido.add(desconto);

        BigDecimal ticket = pedidos.isEmpty()
            ? BigDecimal.ZERO
            : liquido.divide(BigDecimal.valueOf(pedidos.size()), 2, RoundingMode.HALF_UP);

        return new RelatorioVendasDTO(
            inicio,
            fim,
            (long) pedidos.size(),
            bruto,
            desconto,
            liquido,
            ticket,
            qtdPorForma,
            valorPorForma
        );
    }
}